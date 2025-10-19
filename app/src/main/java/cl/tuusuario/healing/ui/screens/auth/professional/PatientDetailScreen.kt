package cl.tuusuario.healing.ui.screens.auth.professional // Asegúrate que el paquete sea correcto

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    // --- ¡CAMBIOS AQUÍ! AÑADIMOS LOS PARÁMETROS NECESARIOS ---
    patientId: String,
    onBack: () -> Unit
) {
    // En una app real, usarías el patientId para buscar los datos del paciente en un ViewModel.
    // Por ahora, usaremos un nombre de ejemplo.
    val patientName = "Juan Pérez González" // Ejemplo

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Resumen", "Medicamentos", "Agenda", "Chat")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        patientName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    // --- ¡CAMBIO AQUÍ! Usamos la función onBack ---
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de llamar */ }) {
                        Icon(Icons.Default.Call, contentDescription = "Llamar")
                    }
                    IconButton(onClick = { /* Acción de menú */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- Barra de Pestañas (Tabs) ---
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }

            // --- Contenido de la Pestaña Seleccionada ---
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTabIndex) {
                    0 -> Text("Aquí irá el resumen del paciente con ID: $patientId") // Usamos el ID
                    1 -> Text("Aquí irá el control de medicamentos.")
                    2 -> Text("Aquí irá la agenda y plan del paciente.")
                    3 -> Text("Aquí irá la pantalla de chat.")
                }
            }
        }
    }
}
