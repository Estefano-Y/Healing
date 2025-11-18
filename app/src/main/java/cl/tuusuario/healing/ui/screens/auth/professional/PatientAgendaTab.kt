package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Modelo de datos de ejemplo para esta pestaña
private sealed class AgendaItem(val date: String, val title: String) {
    class Appointment(date: String, title: String, val professional: String) : AgendaItem(date, title)
    class Task(date: String, title: String, val isCompleted: Boolean) : AgendaItem(date, title)
}

@Composable
fun PatientAgendaTab(
    patientId: String // Recibe el ID para poder buscar los datos
) {
    // --- Lógica de ejemplo: En el futuro, usarías el patientId para obtener estos datos de un ViewModel ---
    val agendaItems = remember {
        listOf(
            AgendaItem.Appointment("25 Oct, 2025", "Control Presión Arterial", "Dr. Apellido"),
            AgendaItem.Task("28 Oct, 2025", "Enviar reporte de glucosa", false),
            AgendaItem.Appointment("05 Nov, 2025", "Consulta General", "Dr. Apellido"),
            AgendaItem.Task("10 Nov, 2025", "Realizar ejercicio cardiovascular", true)
        )
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO: Abrir diálogo para añadir cita o tarea */ },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nuevo Evento") }
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
                    "Próximos Eventos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            items(agendaItems) { item ->
                when (item) {
                    is AgendaItem.Appointment -> AgendaAppointmentCard(item)
                    is AgendaItem.Task -> AgendaTaskCard(item)
                }
            }
        }
    }
}

@Composable
private fun AgendaAppointmentCard(item: AgendaItem.Appointment) {
    AgendaItemCard(
        icon = Icons.Default.Event,
        iconTint = MaterialTheme.colorScheme.primary,
        date = item.date,
        title = item.title,
        subtitle = "Cita con: ${item.professional}"
    )
}

@Composable
private fun AgendaTaskCard(item: AgendaItem.Task) {
    val iconTint = if (item.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
    val subtitle = if (item.isCompleted) "Tarea completada" else "Tarea pendiente"
    AgendaItemCard(
        icon = Icons.Default.TaskAlt,
        iconTint = iconTint,
        date = item.date,
        title = item.title,
        subtitle = subtitle
    )
}


@Composable
private fun AgendaItemCard(
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    date: String,
    title: String,
    subtitle: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.width(16.dp))
            Text(date, style = MaterialTheme.typography.bodySmall)
        }
    }
}
