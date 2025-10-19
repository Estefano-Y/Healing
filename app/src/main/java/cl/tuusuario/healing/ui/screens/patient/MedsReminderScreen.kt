package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
// --- ¡IMPORTS DE LA ARQUITECTURA! ---
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.MedsReminderEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
// Ruta correcta a tu ViewModel, dentro de 'screens'
import cl.tuusuario.healing.ui.screens.viewmodels.MedsReminderViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory

// Enum para agrupar, lo mantenemos porque es útil para la UI
private enum class TimePeriod(val displayName: String) {
    MORNING("☀️ Mañana"),
    AFTERNOON("🕛 Tarde"),
    NIGHT("🌙 Noche")
}

// Función auxiliar para clasificar un recordatorio en un período de tiempo
private fun getTimePeriod(time: String): TimePeriod {
    val hour = time.split(":")[0].toIntOrNull() ?: 0
    return when (hour) {
        in 0..11 -> TimePeriod.MORNING
        in 12..18 -> TimePeriod.AFTERNOON
        else -> TimePeriod.NIGHT
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MedsReminderScreen(onBack: () -> Unit) {

    // --- 1. CONEXIÓN AL VIEWMODEL Y A LA BASE DE DATOS ---
    val context = LocalContext.current
    val repository = remember {
        val db = AppDatabase.getDatabase(context)
        PatientDataRepository(
            db.noteDao(),
            db.personalDataDao(),
            db.emergencyContactDao(),
            db.medsReminderDao()
        )
    }
    val viewModel: MedsReminderViewModel = viewModel(factory = ViewModelFactory(repository))
    // Obtenemos la lista real de recordatorios desde la base de datos
    val allMedications by viewModel.remindersState.collectAsState()

    // --- ESTADO PARA EL DIÁLOGO DE AÑADIR ---
    var showAddDialog by remember { mutableStateOf(false) }

    // Agrupamos los medicamentos que vienen de la BD por período de tiempo
    val groupedMeds = allMedications.groupBy { getTimePeriod(it.time) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Horario de Medicamentos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Recordatorio")
            }
        }
    ) { paddingValues ->
        // Si la lista está vacía, muestra un mensaje
        if (allMedications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay recordatorios. Presiona '+' para añadir uno.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
            ) {
                TimePeriod.values().forEach { period ->
                    groupedMeds[period]?.let { medsInPeriod ->
                        // Título de la sección (Mañana, Tarde, Noche)
                        stickyHeader {
                            Surface(modifier = Modifier.fillParentMaxWidth(), color = MaterialTheme.colorScheme.surface) {
                                Text(
                                    text = period.displayName,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        }

                        // Lista de medicamentos para ese período
                        items(medsInPeriod, key = { it.id }) { medication ->
                            TimelineNode(
                                medication = medication,
                                // onTakenChange ahora llama al ViewModel
                                onTakenChange = { viewModel.toggleIsTaken(medication) },
                                onDelete = { viewModel.deleteReminder(medication.id) },
                                // No dibujamos la línea para el último elemento del grupo
                                drawConnectorLine = medication != medsInPeriod.last()
                            )
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGO PARA AÑADIR UN NUEVO RECORDATORIO ---
    if (showAddDialog) {
        AddReminderDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { medName, dose, time ->
                viewModel.addReminder(medName, dose, time)
                showAddDialog = false
            }
        )
    }
}

// --- COMPOSABLES DE LA UI (TimelineNode, MedicationCard, etc.) ---
// Los he adaptado para que usen `MedsReminderEntity` en lugar de tu `Medication` local.

@Composable
private fun TimelineNode(
    medication: MedsReminderEntity,
    onTakenChange: () -> Unit,
    onDelete: () -> Unit,
    drawConnectorLine: Boolean
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        // Columna de la Línea de Tiempo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Text(medication.time, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            TimelineIndicator(isTaken = medication.isTaken, drawConnectorLine = drawConnectorLine)
        }

        // Tarjeta del Medicamento
        MedicationCard(
            medication = medication,
            onTakenChange = onTakenChange,
            onDelete = onDelete,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun TimelineIndicator(isTaken: Boolean, drawConnectorLine: Boolean) {
    val indicatorColor = if (isTaken) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val connectorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    Canvas(modifier = Modifier.fillMaxHeight()) {
        drawCircle(
            color = indicatorColor,
            radius = 12f,
            center = center.copy(y = 12f)
        )
        if (drawConnectorLine) {
            drawLine(
                color = connectorColor,
                start = center.copy(y = 12f + 12f),
                end = center.copy(y = size.height),
                strokeWidth = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }
    }
}

@Composable
private fun MedicationCard(
    medication: MedsReminderEntity,
    onTakenChange: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = if (medication.isTaken) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    val contentColor = if (medication.isTaken) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Medication, contentDescription = null, tint = contentColor, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medication.medName, fontWeight = FontWeight.Bold, color = contentColor, fontSize = 18.sp)
                Text(text = medication.dose, color = contentColor, fontSize = 14.sp)
            }
            Checkbox(
                checked = medication.isTaken,
                onCheckedChange = { onTakenChange() } // Llamada simple, el VM tiene el contexto
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar recordatorio", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}


@Composable
private fun AddReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, dose: String, time: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nuevo Recordatorio", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre del medicamento") })
                OutlinedTextField(value = dose, onValueChange = { dose = it }, label = { Text("Dosis (ej: 1 comprimido)") })
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Hora (ej: 08:00)") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { onConfirm(name, dose, time) }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
