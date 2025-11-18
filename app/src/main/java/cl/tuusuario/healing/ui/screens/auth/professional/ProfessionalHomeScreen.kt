package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.tuusuario.healing.ui.navigation.Routes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalHomeScreen(nav: NavController) {

    val criticalAlerts = 2
    val pendingMessages = 5
    val nextAppointment = "Hoy a las 15:00 con Juan Pérez"

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

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
        AnimatedVisibility(visible = visible, enter = fadeIn(tween(500))) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInVertically(animationSpec = tween(500)) { -it }
                    ) {
                        Text(
                            "Bienvenido, Dr. Patrick Scrum",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (criticalAlerts > 0) {
                    item {
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(500, 200)) + slideInVertically(tween(500, 200))
                        ) {
                            HighlightCard(
                                title = "$criticalAlerts Alertas Críticas",
                                subtitle = "Pacientes que requieren atención inmediata.",
                                icon = Icons.Default.Warning,
                                color = MaterialTheme.colorScheme.errorContainer,
                                iconColor = MaterialTheme.colorScheme.error,
                                onClick = { nav.navigate(Routes.PROF_PATIENTS) }
                            )
                        }
                    }
                }

                item {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(500, 300)) + slideInVertically(tween(500, 300))
                    ) {
                        InfoCard(
                            title = "Próxima Cita",
                            subtitle = nextAppointment,
                            icon = Icons.Default.CalendarToday,
                            onClick = { nav.navigate(Routes.PROF_AGENDA) }
                        )
                    }
                }

                if (pendingMessages > 0) {
                    item {
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(tween(500, 400)) + slideInVertically(tween(500, 400))
                        ) {
                            InfoCard(
                                title = "$pendingMessages Mensajes Nuevos",
                                subtitle = "Pacientes esperando una respuesta.",
                                icon = Icons.Default.MarkChatRead,
                                onClick = { /* Navegar a una pantalla de mensajería general o al primer paciente con mensaje */ }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(500, 500)) + slideInVertically(tween(500, 500))
                    ) {
                        Text(
                            "Herramientas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                itemsIndexed(
                    listOf(
                        "Ver todos los Pacientes" to Icons.Default.Groups,
                        "Gestionar Agenda" to Icons.Default.Event,
                        "Registrar Atención" to Icons.Default.PostAdd,
                        "Info de medicamentos (FDA)" to Icons.Default.Info,
                        "Mapa" to Icons.Default.Map
                    )
                ) { index, item ->
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(500, 600 + index * 100)) + slideInVertically(tween(500, 600 + index * 100))
                    ) {
                        ActionCard(
                            title = item.first,
                            icon = item.second,
                            onClick = {
                                when (item.first) {
                                    "Ver todos los Pacientes" -> nav.navigate(Routes.PROF_PATIENTS)
                                    "Gestionar Agenda" -> nav.navigate(Routes.PROF_AGENDA)
                                    "Registrar Atención" -> nav.navigate(Routes.PROF_REGISTER)
                                    "Info de medicamentos (FDA)" -> nav.navigate(Routes.DRUG_INFO)
                                    "Mapa" -> nav.navigate(Routes.MAP)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

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
