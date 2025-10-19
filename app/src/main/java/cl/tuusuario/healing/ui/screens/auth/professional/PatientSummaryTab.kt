package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Modelo de datos de ejemplo para esta pestaña
private data class PatientSummaryData(
    val lastCheckIn: String,
    val bloodType: String,
    val mainDiagnosis: String,
    val currentVitals: String
)

@Composable
fun PatientSummaryTab(
    patientId: String // Recibe el ID para poder buscar los datos en el futuro
) {
    // --- Lógica de ejemplo: En el futuro, usarías el patientId para obtener estos datos de un ViewModel ---
    val summaryData = PatientSummaryData(
        lastCheckIn = "Hoy, 09:15 AM",
        bloodType = "O+",
        mainDiagnosis = "Hipertensión Arterial",
        currentVitals = "135/85 mmHg - 75 bpm"
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SummaryInfoCard(
                icon = Icons.Default.CalendarToday,
                title = "Último Contacto / Reporte",
                content = summaryData.lastCheckIn
            )
        }
        item {
            SummaryInfoCard(
                icon = Icons.Default.Bloodtype,
                title = "Grupo Sanguíneo",
                content = summaryData.bloodType
            )
        }
        item {
            SummaryInfoCard(
                icon = Icons.Default.Assignment,
                title = "Diagnóstico Principal",
                content = summaryData.mainDiagnosis
            )
        }
        item {
            SummaryInfoCard(
                icon = Icons.Default.MonitorHeart,
                title = "Signos Vitales Recientes",
                content = summaryData.currentVitals
            )
        }
    }
}

@Composable
private fun SummaryInfoCard(
    icon: ImageVector,
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
