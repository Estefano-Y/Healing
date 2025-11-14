package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.PatientDetailViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    patientId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember {
        val db = AppDatabase.getDatabase(context)
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao(),
            professionalDao = db.professionalDao(),
            userDao = db.userDao()
        )
    }
    val viewModel: PatientDetailViewModel = viewModel(
        factory = ViewModelFactory(repository)
    )

    val patient by viewModel.patientState.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Resumen", "Medicamentos", "Agenda", "Chat")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = patient?.name ?: "Cargando...",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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

            if (patient == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTabIndex) {
                    0 -> PatientSummaryTab(viewModel = viewModel)
                    1 -> PatientMedsTab(patientId = patientId)
                    2 -> PatientAgendaTab(patientId = patientId)
                    3 -> PatientChatTab(patientId = patientId)
                }
            }
        }
    }
}