package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(dataFromDb) {
        visible = true
        if (dataFromDb == null) {
            isInEditMode = true
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Datos Personales") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                modifier = Modifier.alpha(if (scrollState.value > 100) 0.7f else 1.0f) // Parallax
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isInEditMode) {
                        if (viewModel.isFormValid) {
                            viewModel.savePersonalData()
                            scope.launch {
                                snackbarHostState.showSnackbar("Datos Guardados")
                            }
                             isInEditMode = false
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(viewModel.nameError ?: "Hay errores en el formulario")
                            }
                        }
                    } else {
                        isInEditMode = true
                    }
                }
            ) {
                Crossfade(targetState = isInEditMode, label = "FAB_Icon") {
                    if (it) Icon(Icons.Default.Done, "Guardar") else Icon(Icons.Default.Edit, "Editar")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInHorizontally(initialOffsetX = { -it / 2 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Crossfade(targetState = isInEditMode, label = "DataFields") {
                    if(it) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            EditFields(
                                viewModel = viewModel,
                                name = viewModel.name,
                                onNameChange = { viewModel.onNameChange(it) },
                                birthDate = viewModel.birthDate,
                                onBirthChange = { viewModel.onBirthDateChange(it) },
                                bloodType = viewModel.bloodType,
                                onBloodChange = { viewModel.onBloodTypeChange(it) },
                                allergies = viewModel.allergies,
                                onAllergiesChange = { viewModel.onAllergiesChange(it) }
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            DisplayFields(
                                name = dataFromDb?.name,
                                birthDate = dataFromDb?.birthDate,
                                bloodType = dataFromDb?.bloodType,
                                allergies = dataFromDb?.allergies
                            )
                        }
                    }
                }
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun EditFields(
    viewModel: PersonalDataViewModel,
    name: String,
    onNameChange: (String) -> Unit,
    birthDate: String,
    onBirthChange: (String) -> Unit,
    bloodType: String,
    onBloodChange: (String) -> Unit,
    allergies: String,
    onAllergiesChange: (String) -> Unit
) {
    SectionTitle("Información Básica")
    EditableDataField(
        label = "Nombre",
        value = name,
        onValueChange = onNameChange,
        leadingIcon = Icons.Default.Person,
        isError = viewModel.nameError != null,
        errorMessage = viewModel.nameError
    )
    EditableDataField(label = "Fecha Nacimiento", value = birthDate, onValueChange = onBirthChange, leadingIcon = Icons.Default.Cake)
    EditableDataField(label = "Tipo de Sangre", value = bloodType, onValueChange = onBloodChange, leadingIcon = Icons.Default.Bloodtype)
    SectionTitle("Datos Médicos")
    EditableDataField(label = "Alergias", value = allergies, onValueChange = onAllergiesChange, leadingIcon = Icons.Default.MedicalInformation, singleLine = false)
}

@Composable
private fun DisplayFields(name: String?, birthDate: String?, bloodType: String?, allergies: String?){
    SectionTitle("Información Básica")
    DisplayDataField(label = "Nombre", value = name, icon = Icons.Default.Person)
    DisplayDataField(label = "Fecha Nacimiento", value = birthDate, icon = Icons.Default.Cake)
    DisplayDataField(label = "Tipo de Sangre", value = bloodType, icon = Icons.Default.Bloodtype)
    SectionTitle("Datos Médicos")
    DisplayDataField(label = "Alergias", value = allergies, icon = Icons.Default.MedicalInformation)
}


@Composable
private fun SectionTitle(title: String) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    AnimatedVisibility(visible = visible, enter = slideInHorizontally(animationSpec = tween(500)))
    { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) }
}

@Composable
private fun EditableDataField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(leadingIcon, null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            isError = isError
        )
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
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
