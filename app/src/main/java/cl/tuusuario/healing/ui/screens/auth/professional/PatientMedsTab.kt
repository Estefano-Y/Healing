package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Modelo de datos de ejemplo para esta pestaña
private data class MedicationStatus(
    val id: String,
    val name: String,
    val dose: String,
    val schedule: String,
    val isTakenToday: Boolean
)

@Composable
fun PatientMedsTab(
    patientId: String // Recibe el ID para poder buscar los datos
) {
    // --- Lógica de ejemplo: En el futuro, usarías el patientId para obtener estos datos de un ViewModel ---
    val medications = remember {
        listOf(
            MedicationStatus("m01", "Losartán", "50 mg", "Cada 12 horas", true),
            MedicationStatus("m02", "Aspirina", "100 mg", "Una vez al día", true),
            MedicationStatus("m03", "Atorvastatina", "20 mg", "Cada noche", false)
        )
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO: Abrir diálogo para añadir prescripción */ },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Prescripción") }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Tratamiento Actual",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            items(medications, key = { it.id }) { med ->
                MedStatusCard(med = med)
            }
        }
    }
}

@Composable
private fun MedStatusCard(med: MedicationStatus) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Medication,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(med.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${med.dose} - ${med.schedule}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.width(16.dp))
            // Indicador de estado
            val statusText = if (med.isTakenToday) "Tomado Hoy" else "Pendiente"
            val statusColor = if (med.isTakenToday) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
            Text(statusText, color = statusColor, fontWeight = FontWeight.SemiBold)
        }
    }
}
