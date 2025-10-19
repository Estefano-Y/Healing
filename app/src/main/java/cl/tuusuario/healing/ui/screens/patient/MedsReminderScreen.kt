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
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.MedsReminderEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.MedsReminderViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory

// --- ¡CORRECCIÓN! Se restaura el contenido de estas funciones auxiliares ---
private enum class TimePeriod(val displayName: String) {
    MORNING("☀️ Mañana"),
    AFTERNOON("🕛 Tarde"),
    NIGHT("🌙 Noche")
}

private fun getTimePeriod(time: String): TimePeriod {
    val hour = time.split(":")[0].toIntOrNull() ?: 0
    return when (hour) {
        in 0..11 -> TimePeriod.MORNING
        in 12..18 -> TimePeriod.AFTERNOON
        else -> TimePeriod.NIGHT
    }
}
// --- FIN DE LA CORRECCIÓN ---


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MedsReminderScreen(onBack: () -> Unit) {

    // Esta parte ya está correcta
    val context = LocalContext.current
    val repository = remember {
        val db = AppDatabase.getDatabase(context)
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao(),
            professionalDao = db.professionalDao()
        )
    }

    val viewModel: MedsReminderViewModel = viewModel(factory = ViewModelFactory(repository))
    val allMedications by viewModel.remindersState.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

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
                        stickyHeader {
                            Surface(modifier = Modifier.fillParentMaxWidth(), color = MaterialTheme.colorScheme.surface) {
                                Text(
                                    text = period.displayName,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        }
                        items(medsInPeriod, key = { it.id }) { medication ->
                            TimelineNode(
                                medication = medication,
                                onTakenChange = { viewModel.toggleIsTaken(medication) },
                                onDelete = { viewModel.deleteReminder(medication.id) },
                                drawConnectorLine = medication != medsInPeriod.last()
                            )
                        }
                    }
                }
            }
        }
    }

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

// --- ¡CORRECCIÓN! Se restaura el contenido de los Composables privados ---
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Text(medication.time, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            TimelineIndicator(isTaken = medication.isTaken, drawConnectorLine = drawConnectorLine)
        }
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
                onCheckedChange = { onTakenChange() }
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
