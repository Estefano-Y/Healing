package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.tuusuario.healing.ui.navigation.Routes
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Modelo para los items de acción en la cuadrícula
private data class ActionItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHomeScreen(nav: NavController) {

    // --- Datos de ejemplo que vendrían de un ViewModel ---
    val patientName = "Juan" // Nombre del paciente
    val nextTask = "Tomar Paracetamol - 10:00 AM"
    val dayFormatter = remember {
        DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "ES"))
    }
    val today = LocalDate.now().format(dayFormatter).replaceFirstChar { it.uppercase() }

    // Lista de acciones que el paciente puede realizar
    val actions = remember {
        listOf(
            ActionItem("Mi Agenda", Icons.Filled.CalendarMonth, Routes.CALENDAR),
            ActionItem("Mis Medicamentos", Icons.Filled.Medication, Routes.MEDS),
            ActionItem("Contacto de Emergencia", Icons.Filled.Emergency, Routes.EMERGENCY),
            ActionItem("Mis Datos", Icons.Filled.AccountCircle, Routes.PERSONAL),
            ActionItem("Mis Notas", Icons.Filled.Notes, Routes.NOTES),
            // Puedes añadir más acciones aquí
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bienestar") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- 1. Saludo y Fecha ---
            Column {
                Text(
                    text = "Hola, $patientName",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = today,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // --- 2. Tarjeta de Próxima Tarea ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Próxima tarea",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Siguiente tarea de hoy:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = nextTask,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            // --- 3. Cuadrícula de Acciones ---
            Text(
                "Tus Herramientas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            // Usamos LazyVerticalGrid para crear una cuadrícula de 2 columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(actions) { action ->
                    ActionCard(
                        title = action.title,
                        icon = action.icon,
                        onClick = { nav.navigate(action.route) }
                    )
                }
            }
        }
    }
}


// Componente para las tarjetas de acción en la cuadrícula
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f), // Hace que la tarjeta sea cuadrada
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
