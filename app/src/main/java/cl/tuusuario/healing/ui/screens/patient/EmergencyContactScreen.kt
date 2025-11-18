package cl.tuusuario.healing.ui.screens.patient

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
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
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.EmergencyContactViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
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
    val viewModel: EmergencyContactViewModel = viewModel(factory = ViewModelFactory(repository))
    val contactFromDb by viewModel.emergencyContactState.collectAsState()

    var isInEditMode by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(contactFromDb) {
        visible = true
        if (contactFromDb != null) {
            contactName = contactFromDb!!.name
            relationship = contactFromDb!!.relationship
            phoneNumber = contactFromDb!!.phone
        } else {
            isInEditMode = true
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacto de Emergencia") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                modifier = Modifier.alpha(if (scrollState.value > 100) 0.7f else 1.0f)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isInEditMode) {
                        viewModel.saveEmergencyContact(contactName, relationship, phoneNumber)
                        scope.launch {
                            snackbarHostState.showSnackbar("Contacto Guardado")
                        }
                    }
                    if (contactFromDb != null) {
                        isInEditMode = !isInEditMode
                    }
                }
            ) {
                Crossfade(targetState = isInEditMode) { isEditing ->
                    if (isEditing) {
                        Icon(Icons.Default.Done, contentDescription = "Guardar")
                    } else {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Crossfade(targetState = isInEditMode, label = "ViewEditCrossfade") { isEditing ->
                    if (isEditing) {
                        EditContactForm(
                            name = contactName,
                            onNameChange = { contactName = it },
                            relationship = relationship,
                            onRelationshipChange = { relationship = it },
                            phone = phoneNumber,
                            onPhoneChange = { phoneNumber = it }
                        )
                    } else {
                        contactFromDb?.let {
                            AnimatedVisibility(visible = true, enter = slideInHorizontally())
                            { ViewContactCard(
                                name = it.name,
                                relationship = it.relationship,
                                phone = it.phone,
                                onCall = {
                                    val intent = Intent(Intent.ACTION_DIAL, "tel:${it.phone}".toUri())
                                    context.startActivity(intent)
                                },
                                onMessage = {
                                    val intent = Intent(Intent.ACTION_SENDTO, "smsto:${it.phone}".toUri())
                                    context.startActivity(intent)
                                }
                            ) }
                        }
                    }
                }
                if (!isInEditMode && contactFromDb == null) {
                    Text("No hay un contacto de emergencia guardado. Presiona el botón de editar para añadir uno.")
                }
            }
        }
    }
}

@Composable
private fun ViewContactCard(
    name: String,
    relationship: String,
    phone: String,
    onCall: () -> Unit,
    onMessage: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(text = name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = relationship, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(text = phone, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(text = "Llamar", icon = Icons.Default.Call, onClick = onCall, modifier = Modifier.weight(1f))
                ActionButton(text = "Mensaje", icon = Icons.AutoMirrored.Filled.Message, onClick = onMessage, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun EditContactForm(
    name: String, onNameChange: (String) -> Unit,
    relationship: String, onRelationshipChange: (String) -> Unit,
    phone: String, onPhoneChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = name, onValueChange = onNameChange, label = { Text("Nombre del Contacto") },
            modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        OutlinedTextField(
            value = relationship, onValueChange = onRelationshipChange, label = { Text("Relación (Ej: Madre, Médico)") },
            modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) }
        )
        OutlinedTextField(
            value = phone, onValueChange = onPhoneChange, label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
        )
    }
}

@Composable
private fun ActionButton(text: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick, modifier = modifier.height(50.dp), shape = RoundedCornerShape(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.Bold)
        }
    }
}
