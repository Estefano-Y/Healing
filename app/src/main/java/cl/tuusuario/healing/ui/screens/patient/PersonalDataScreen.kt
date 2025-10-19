package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
// --- ¡IMPORTS NECESARIOS PARA LA ARQUITECTURA! ---
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
// Recuerda que tu ruta es 'screens.viewmodels'
import cl.tuusuario.healing.ui.screens.viewmodels.PersonalDataViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    onBack: () -> Unit
) {
    // --- 1. OBTENCIÓN DE DEPENDENCIAS Y VIEWMODEL (LA PARTE CLAVE) ---
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = remember {
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao()
        )
    }
    val viewModel: PersonalDataViewModel = viewModel(factory = ViewModelFactory(repository))
    val dataFromDb by viewModel.personalDataState.collectAsState()

    // --- ESTADOS LOCALES DE LA UI ---
    var isInEditMode by remember { mutableStateOf(false) }

    // Estados para cada campo del formulario. Ahora se inicializan vacíos.
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    // Tu entidad PersonalDataEntity no tiene phone, email, ni observations.
    // Los mantenemos como estado local por ahora.
    var allergies by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }


    // Este efecto se ejecuta cuando los datos de la BD cambian.
    // Rellena los campos de la UI con los datos persistidos.
    LaunchedEffect(dataFromDb) {
        dataFromDb?.let { data ->
            name = data.name
            birthDate = data.birthDate
            bloodType = data.bloodType
            allergies = data.allergies
            // Aquí podrías cargar phone, email, etc., si los añadieras a la Entidad.
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isInEditMode) {
                        // --- ¡LÓGICA DE GUARDADO REAL! ---
                        // Llamamos al ViewModel para guardar los datos en la base de datos.
                        viewModel.savePersonalData(
                            name = name,
                            birthDate = birthDate,
                            bloodType = bloodType,
                            allergies = allergies
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar("Datos guardados correctamente")
                        }
                    }
                    // Cambiamos el modo de edición
                    isInEditMode = !isInEditMode
                }
            ) {
                Crossfade(targetState = isInEditMode, label = "fab_icon_animation") { isEditing ->
                    if (isEditing) {
                        Icon(Icons.Default.Done, contentDescription = "Guardar")
                    } else {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            }
        }
    ) { paddingValues ->
        // --- EL RESTO DE TU UI ES PERFECTO Y SE MANTIENE IGUAL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            SectionTitle("Información Básica")
            EditableDataField(
                label = "Nombre Completo",
                value = name,
                onValueChange = { name = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            EditableDataField(
                label = "Fecha de Nacimiento",
                value = birthDate,
                onValueChange = { birthDate = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                keyboardType = KeyboardType.Text // Cambiado a texto para flexibilidad
            )
            EditableDataField(
                label = "Tipo de Sangre",
                value = bloodType,
                onValueChange = { bloodType = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Bloodtype, contentDescription = null) }
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle("Información de Contacto (No guardado en BD aún)")
            EditableDataField(
                label = "Teléfono",
                value = phone,
                onValueChange = { phone = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardType = KeyboardType.Phone
            )
            EditableDataField(
                label = "Correo Electrónico",
                value = email,
                onValueChange = { email = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle("Datos Médicos Adicionales")
            EditableDataField(
                label = "Alergias Conocidas",
                value = allergies,
                onValueChange = { allergies = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.MedicalInformation, contentDescription = null) }
            )
            EditableDataField(
                label = "Observaciones Médicas (No guardado en BD aún)",
                value = observations,
                onValueChange = { observations = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Note, contentDescription = null) },
                singleLine = false
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

// Tus composables privados (SectionTitle y EditableDataField) son perfectos y no necesitan cambios.
@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun EditableDataField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    editMode: Boolean,
    leadingIcon: @Composable () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    AnimatedContent(
        targetState = editMode,
        label = "editable_field_animation",
        modifier = Modifier.padding(vertical = 8.dp)
    ) { isEditing ->
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                leadingIcon = leadingIcon,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = singleLine
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    leadingIcon()
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    // Muestra un guion si el valor está vacío, para que no se vea raro en modo vista
                    Text(value.ifEmpty { "-" }, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 2.dp))
                }
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }
    }
}
