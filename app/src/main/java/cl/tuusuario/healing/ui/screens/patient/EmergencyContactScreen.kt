package cl.tuusuario.healing.ui.screens.patient

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import cl.tuusuario.healing.ui.screens.viewmodels.EmergencyContactViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
    onBack: () -> Unit
) {
    // Tu lógica de conexión a la base de datos y estados está perfecta.
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember {
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao(),
            professionalDao = db.professionalDao()
        )
    }
    val viewModel: EmergencyContactViewModel = viewModel(factory = ViewModelFactory(repository))
    val contactFromDb by viewModel.emergencyContactState.collectAsState()
    val mainContact = contactFromDb

    var isInEditMode by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(mainContact) {
        if (mainContact != null) {
            contactName = mainContact.name
            relationship = mainContact.relationship
            phoneNumber = mainContact.phone
        } else {
            isInEditMode = true
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        // ... Tu Scaffold, TopAppBar y FAB se quedan igual ...
        topBar = { /* ... */ },
        floatingActionButton = { /* ... */ },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            // ... Tu Column y AnimatedVisibility se quedan igual ...
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = !isInEditMode && mainContact != null) {
                ViewContactCard(
                    name = mainContact?.name ?: "",
                    relationship = mainContact?.relationship ?: "",
                    phone = mainContact?.phone ?: "",
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${mainContact?.phone}"))
                        context.startActivity(intent)
                    },
                    onMessage = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${mainContact?.phone}"))
                        context.startActivity(intent)
                    }
                )
            }
            AnimatedVisibility(visible = isInEditMode) {
                EditContactForm(
                    name = contactName,
                    onNameChange = { contactName = it },
                    relationship = relationship,
                    onRelationshipChange = { relationship = it },
                    phone = phoneNumber,
                    onPhoneChange = { phoneNumber = it }
                )
            }
            if (!isInEditMode && mainContact == null) {
                Text("No hay un contacto de emergencia guardado. Presiona el botón de editar para añadir uno.")
            }
        }
    }
}


// --- ¡¡¡CORRECCIÓN!!! SE RESTAURA EL CÓDIGO DE ESTAS FUNCIONES ---

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
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = relationship,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = phone,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(
                    text = "Llamar",
                    icon = Icons.Default.Call,
                    onClick = onCall,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = "Mensaje",
                    icon = Icons.Default.Message,
                    onClick = onMessage,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EditContactForm(
    name: String,
    onNameChange: (String) -> Unit,
    relationship: String,
    onRelationshipChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre del Contacto") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        OutlinedTextField(
            value = relationship,
            onValueChange = onRelationshipChange,
            label = { Text("Relación (Ej: Madre, Médico)") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) }
        )
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.Bold)
        }
    }
}
