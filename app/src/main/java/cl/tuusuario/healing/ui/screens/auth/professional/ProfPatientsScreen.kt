// La ruta de tu paquete podría ser diferente, asegúrate de que coincida
package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.tuusuario.healing.R // Asegúrate que este import sea correcto

// --- Modelo de Datos para esta pantalla ---
private enum class PatientStatus(val color: Color, val icon: ImageVector) {
    CRITICAL(Color(0xFFE57373), Icons.Default.Warning),
    ATTENTION(Color(0xFFFFA726), Icons.Default.ChatBubble),
    OK(Color.Transparent, Icons.Default.CheckCircle)
}

private data class PatientSummary(
    val id: String,
    val name: String,
    val profilePicRes: Int,
    val status: PatientStatus,
    val lastActivity: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfPatientsScreen(
    // --- ¡CAMBIOS AQUÍ! AÑADIMOS LOS NUEVOS PARÁMETROS ---
    onPatientClick: (patientId: String) -> Unit,
    onAddPatientClick: () -> Unit,
    onBack: () -> Unit
) {
    // --- Datos de ejemplo. Vendrían de un ViewModel ---
    val allPatients = remember {
        listOf(
            PatientSummary("1", "Juan Pérez González", R.drawable.ic_launcher_background, PatientStatus.CRITICAL, "No ha registrado medicamentos hoy"),
            PatientSummary("2", "Ana María López", R.drawable.ic_launcher_background, PatientStatus.ATTENTION, "Nuevo mensaje hace 1 hora"),
            PatientSummary("3", "Carlos Soto Martínez", R.drawable.ic_launcher_background, PatientStatus.OK, "Próxima cita: Mañana, 10:00"),
            PatientSummary("4", "Luisa Fernández Díaz", R.drawable.ic_launcher_background, PatientStatus.OK, "Última actividad: Ayer"),
            PatientSummary("5", "Pedro Morales Castro", R.drawable.ic_launcher_background, PatientStatus.ATTENTION, "Resultados de análisis disponibles"),
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredPatients = remember(searchQuery, allPatients) {
        if (searchQuery.isBlank()) {
            allPatients
        } else {
            allPatients.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pacientes") },
                // ¡CAMBIO! Añadimos el botón de "atrás" que recibe por parámetro
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                // --- ¡CAMBIO AQUÍ! ---
                onClick = onAddPatientClick // Usamos la nueva función
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Paciente")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- Barra de Búsqueda ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar paciente...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    AnimatedVisibility(visible = searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // --- Lista de Pacientes ---
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPatients, key = { it.id }) { patient ->
                    PatientCard(
                        patient = patient,
                        // --- ¡CAMBIO AQUÍ! ---
                        onClick = { onPatientClick(patient.id) } // Usamos la nueva función
                    )
                }
            }
        }
    }
}

// --- La tarjeta de resumen por paciente ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PatientCard(
    patient: PatientSummary,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = patient.profilePicRes),
                contentDescription = "Foto de ${patient.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = patient.lastActivity,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(12.dp))

            AnimatedVisibility(visible = patient.status != PatientStatus.OK) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(patient.status.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = patient.status.icon,
                        contentDescription = "Estado",
                        tint = patient.status.color,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
