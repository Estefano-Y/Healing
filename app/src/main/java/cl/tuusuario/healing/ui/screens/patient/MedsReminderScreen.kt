package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Modelo de datos para la pantalla ---
private data class Medication(
    val id: Int,
    val name: String,
    val dose: String,
    val time: String,
    val timePeriod: TimePeriod
)

private enum class TimePeriod(val displayName: String) {
    MORNING("☀️ Mañana"),
    AFTERNOON("🕛 Tarde"),
    NIGHT("🌙 Noche")
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MedsReminderScreen(onBack: () -> Unit) {

    // --- Datos de ejemplo. En una app real, vendrían de un ViewModel ---
    val allMedications = remember {
        listOf(
            Medication(1, "Paracetamol", "1 comprimido (500mg)", "08:00", TimePeriod.MORNING),
            Medication(2, "Vitamina D", "1 cápsula", "08:00", TimePeriod.MORNING),
            Medication(3, "Ibuprofeno", "1 comprimido (200mg)", "14:30", TimePeriod.AFTERNOON),
            Medication(4, "Melatonina", "1 comprimido (5mg)", "22:00", TimePeriod.NIGHT),
        )
    }

    // Estado para saber qué medicamentos ya se tomaron
    var takenMedicationIds by remember { mutableStateOf(setOf<Int>()) }

    // Agrupamos los medicamentos por período de tiempo
    val groupedMeds = allMedications.groupBy { it.timePeriod }

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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Iteramos sobre los períodos de tiempo definidos para mantener el orden
            TimePeriod.values().forEach { period ->
                groupedMeds[period]?.let { medsInPeriod ->
                    // Mostramos el título de la sección (Mañana, Tarde, Noche)
                    item {
                        Text(
                            text = period.displayName,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    // Mostramos la lista de medicamentos para ese período
                    itemsIndexed(medsInPeriod) { index, medication ->
                        TimelineNode(
                            medication = medication,
                            isTaken = takenMedicationIds.contains(medication.id),
                            onTakenChange = { isChecked ->
                                takenMedicationIds = if (isChecked) {
                                    takenMedicationIds + medication.id
                                } else {
                                    takenMedicationIds - medication.id
                                }
                            },
                            // Dibuja la línea solo si no es el último item del grupo
                            drawConnectorLine = index < medsInPeriod.size - 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineNode(
    medication: Medication,
    isTaken: Boolean,
    onTakenChange: (Boolean) -> Unit,
    drawConnectorLine: Boolean
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min) // Asegura que el Row tenga una altura para dibujar la línea
    ) {
        // --- Columna de la Línea de Tiempo ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(60.dp)
        ) {
            Text(medication.time, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            TimelineIndicator(isTaken = isTaken, drawConnectorLine = drawConnectorLine)
        }

        // --- Tarjeta del Medicamento ---
        MedicationCard(
            medication = medication,
            isTaken = isTaken,
            onTakenChange = onTakenChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun TimelineIndicator(isTaken: Boolean, drawConnectorLine: Boolean) {
    val indicatorColor = if (isTaken) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val connectorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    Canvas(modifier = Modifier.fillMaxHeight()) {
        // Dibuja el círculo indicador
        drawCircle(
            color = indicatorColor,
            radius = 12f,
            center = center.copy(y = 12f)
        )

        // Dibuja la línea conectora si es necesario
        if (drawConnectorLine) {
            drawLine(
                color = connectorColor,
                start = center.copy(y = 12f + 12f), // Empieza debajo del círculo
                end = center.copy(y = size.height), // Termina al final del composable
                strokeWidth = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) // Línea punteada
            )
        }
    }
}

@Composable
private fun MedicationCard(
    medication: Medication,
    isTaken: Boolean,
    onTakenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isTaken) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    val contentColor = if (isTaken) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Medication,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    fontSize = 18.sp
                )
                Text(
                    text = medication.dose,
                    color = contentColor,
                    fontSize = 14.sp
                )
            }
            Checkbox(
                checked = isTaken,
                onCheckedChange = onTakenChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
