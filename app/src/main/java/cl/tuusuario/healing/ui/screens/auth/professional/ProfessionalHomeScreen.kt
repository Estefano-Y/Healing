package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.tuusuario.healing.ui.navigation.Routes

// Esta pantalla ahora recibe el NavController para poder navegar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalHomeScreen(nav: NavController) {

    // Datos de ejemplo que vendrían de un ViewModel
    val criticalAlerts = 2
    val pendingMessages = 5
    val nextAppointment = "Hoy a las 15:00 con Juan Pérez"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Control") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        // Usamos LazyColumn para que la pantalla pueda crecer si añadimos más tarjetas
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Saludo y resumen rápido
            item {
                Text(
                    "Bienvenido, Dr. Apellido", // Reemplazar con el nombre real
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tarjeta de Alertas Críticas
            if (criticalAlerts > 0) {
                item {
                    HighlightCard(
                        title = "$criticalAlerts Alertas Críticas",
                        subtitle = "Pacientes que requieren atención inmediata.",
                        icon = Icons.Default.Warning,
                        color = MaterialTheme.colorScheme.errorContainer,
                        iconColor = MaterialTheme.colorScheme.error,
                        onClick = {
                            // Navegar a la lista de pacientes, quizás con un filtro de "críticos"
                            nav.navigate(Routes.PROF_PATIENTS)
                        }
                    )
                }
            }

            // Tarjeta de Próxima Cita
            item {
                InfoCard(
                    title = "Próxima Cita",
                    subtitle = nextAppointment,
                    icon = Icons.Default.CalendarToday,
                    onClick = { nav.navigate(Routes.PROF_AGENDA) }
                )
            }

            // Tarjeta de Mensajes Pendientes
            if (pendingMessages > 0) {
                item {
                    InfoCard(
                        title = "$pendingMessages Mensajes Nuevos",
                        subtitle = "Pacientes esperando una respuesta.",
                        icon = Icons.Default.MarkChatRead,
                        onClick = { /* Navegar a una pantalla de mensajería general o al primer paciente con mensaje */ }
                    )
                }
            }

            // Tarjetas de Acceso Rápido a las Herramientas
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Herramientas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                ActionCard(
                    title = "Ver todos los Pacientes",
                    icon = Icons.Default.Groups,
                    onClick = { nav.navigate(Routes.PROF_PATIENTS) }
                )
            }

            item {
                ActionCard(
                    title = "Gestionar Agenda",
                    icon = Icons.Default.Event,
                    onClick = { nav.navigate(Routes.PROF_AGENDA) }
                )
            }

            item {
                ActionCard(
                    title = "Registrar Atención",
                    icon = Icons.Default.PostAdd,
                    onClick = { nav.navigate(Routes.PROF_REGISTER) }
                )
            }
        }
    }
}

// ---- Componentes reutilizables para esta pantalla ----

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HighlightCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    iconColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoCard(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
