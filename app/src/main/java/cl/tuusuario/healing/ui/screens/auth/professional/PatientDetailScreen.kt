package cl.tuusuario.healing.ui.screens.auth.professional

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

// --- ¡CORRECCIÓN! AÑADE EL IMPORT PARA LA NUEVA PESTAÑA ---
import cl.tuusuario.healing.ui.screens.auth.professional.PatientAgendaTab
import cl.tuusuario.healing.ui.screens.auth.professional.PatientMedsTab
import cl.tuusuario.healing.ui.screens.auth.professional.PatientSummaryTab
import cl.tuusuario.healing.ui.screens.auth.professional.PatientChatTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    patientId: String,
    onBack: () -> Unit
) {
    val patientName = "Juan Pérez González"

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
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }

            // --- ¡CORRECCIÓN! AÑADE EL CASO PARA LA PESTAÑA 2 ---
            when (selectedTabIndex) {
                0 -> {
                    PatientSummaryTab(patientId = patientId)
                }
                1 -> {
                    PatientMedsTab(patientId = patientId)
                }
                2 -> {
                    // Reemplazamos el Text por nuestro nuevo Composable
                    PatientAgendaTab(patientId = patientId)
                }
                3 -> PatientChatTab(patientId = patientId)

            }
        }
    }
}
