package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.PersonalDataViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
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
    val viewModel: PersonalDataViewModel = viewModel(factory = ViewModelFactory(repository))
    val dataFromDb by viewModel.personalDataState.collectAsState()

    var isInEditMode by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }

    LaunchedEffect(dataFromDb) {
        dataFromDb?.let {
            name = it.name
            birthDate = it.birthDate
            bloodType = it.bloodType
            allergies = it.allergies
        } ?: run {
            isInEditMode = true
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Datos Personales") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isInEditMode) {
                        viewModel.savePersonalData(name, birthDate, bloodType, allergies)
                        scope.launch {
                            snackbarHostState.showSnackbar("Datos Guardados")
                        }
                    }
                    isInEditMode = !isInEditMode
                }
            ) {
                Crossfade(targetState = isInEditMode) {
                    if (it) Icon(Icons.Default.Done, "Guardar") else Icon(Icons.Default.Edit, "Editar")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Crossfade(targetState = isInEditMode, label = "DataFields") {
                if(it) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        EditFields(name, {name=it}, birthDate, {birthDate=it}, bloodType, {bloodType=it}, allergies, {allergies=it})
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        DisplayFields(dataFromDb?.name, dataFromDb?.birthDate, dataFromDb?.bloodType, dataFromDb?.allergies)
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun EditFields(name: String, onNameChange: (String) -> Unit, birthDate: String, onBirthChange: (String) -> Unit, blood: String, onBloodChange: (String) -> Unit, allergies: String, onAllergiesChange: (String) -> Unit) {
    SectionTitle("Información Básica")
    EditableDataField(label = "Nombre", value = name, onValueChange = onNameChange, leadingIcon = Icons.Default.Person)
    EditableDataField(label = "Fecha Nacimiento", value = birthDate, onValueChange = onBirthChange, leadingIcon = Icons.Default.Cake)
    EditableDataField(label = "Tipo de Sangre", value = blood, onValueChange = onBloodChange, leadingIcon = Icons.Default.Bloodtype)
    SectionTitle("Datos Médicos")
    EditableDataField(label = "Alergias", value = allergies, onValueChange = onAllergiesChange, leadingIcon = Icons.Default.MedicalInformation, singleLine = false)
}

@Composable
private fun DisplayFields(name: String?, birthDate: String?, blood: String?, allergies: String?){
    SectionTitle("Información Básica")
    DisplayDataField(label = "Nombre", value = name, icon = Icons.Default.Person)
    DisplayDataField(label = "Fecha Nacimiento", value = birthDate, icon = Icons.Default.Cake)
    DisplayDataField(label = "Tipo de Sangre", value = blood, icon = Icons.Default.Bloodtype)
    SectionTitle("Datos Médicos")
    DisplayDataField(label = "Alergias", value = allergies, icon = Icons.Default.MedicalInformation)
}


@Composable
private fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
}

@Composable
private fun EditableDataField(label: String, value: String, onValueChange: (String) -> Unit, leadingIcon: ImageVector, singleLine: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(leadingIcon, null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine
    )
}

@Composable
private fun DisplayDataField(label: String, value: String?, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value?.ifEmpty { "No especificado" } ?: "No especificado", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
