package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
// --- ¡IMPORTS PARA LA ARQUITECTURA DE DATOS! ---
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.PatientDetailViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory

// (Aquí se mantienen los imports de tus pestañas)
import cl.tuusuario.healing.ui.screens.auth.professional.PatientAgendaTab
import cl.tuusuario.healing.ui.screens.auth.professional.PatientMedsTab
import cl.tuusuario.healing.ui.screens.auth.professional.PatientSummaryTab
import cl.tuusuario.healing.ui.screens.auth.professional.PatientChatTab
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    // El patientId y onBack se mantienen, son correctos.
    patientId: String,
    onBack: () -> Unit
) {
    // --- 1. CONEXIÓN AL VIEWMODEL ---
    val context = LocalContext.current
    val repository = remember {
        val db = AppDatabase.getDatabase(context)
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao(),
            professionalDao = db.professionalDao()
        )
    }
    // La creación del ViewModel es un poco diferente aquí para que reciba el patientId.
    val viewModel: PatientDetailViewModel = viewModel(
        factory = ViewModelFactory(repository)
    )

    // --- 2. OBTENEMOS LOS DATOS DEL PACIENTE DESDE EL VIEWMODEL ---
    val patient by viewModel.patientState.collectAsState()

    // El estado de las pestañas se queda igual
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Resumen", "Medicamentos", "Agenda", "Chat")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        // Usamos el nombre del paciente de la base de datos.
                        // Si aún no ha cargado, muestra "Cargando...".
                        text = patient?.name ?: "Cargando...",
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
                    // Estas acciones se mantienen igual
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
            // El TabRow se mantiene igual
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }

            // --- 3. CONTENIDO DE LAS PESTAÑAS (CON ESTADO DE CARGA) ---
            // Si el paciente aún no ha cargado, mostramos un indicador de progreso.
            if (patient == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Una vez que el paciente ha cargado, mostramos el contenido de la pestaña seleccionada.
                // Tu 'when' se mantiene, ¡ya está perfecto!
                when (selectedTabIndex) {
                    0 -> {
                        // Ahora le podríamos pasar el objeto completo si fuera necesario.
                        PatientSummaryTab(patientId = patientId)
                    }
                    1 -> {
                        PatientMedsTab(patientId = patientId)
                    }
                    2 -> {
                        PatientAgendaTab(patientId = patientId)
                    }
                    3 -> PatientChatTab(patientId = patientId)
                }
            }
        }
    }
}
