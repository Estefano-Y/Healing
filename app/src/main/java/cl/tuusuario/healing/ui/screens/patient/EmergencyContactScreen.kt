package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
    onBack: () -> Unit,
    viewModel: EmergencyContactViewModel = viewModel()
) {
    val contacts by viewModel.contacts

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contactos de Emergencia") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onShowAddContactDialog() }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Contacto")
            }
        }
    ) { paddingValues ->
        // Si el diálogo debe mostrarse, lo componemos
        if (viewModel.showAddContactDialog) {
            AddContactDialog(viewModel = viewModel)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (contacts.isEmpty()) {
                item {
                    Text(
                        "No tienes contactos de emergencia.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(contacts) { contact ->
                    ContactItem(
                        contact = contact,
                        onDelete = { viewModel.removeContact(contact) }
                    )
                }
            }
        }
    }
}

// Composable para un solo item en la lista
@Composable
fun ContactItem(contact: Contact, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${contact.relationship} - ${contact.phone}", fontSize = 14.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// Composable para el diálogo de agregar contacto
@Composable
fun AddContactDialog(viewModel: EmergencyContactViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.onDismissAddContactDialog() },
        title = { Text("Añadir Contacto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.newContactName,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = viewModel.newContactPhone,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    label = { Text("Teléfono") }
                )
                OutlinedTextField(
                    value = viewModel.newContactRelationship,
                    onValueChange = { viewModel.onRelationshipChange(it) },
                    label = { Text("Relación (Ej: Madre)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { viewModel.addContact() }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.onDismissAddContactDialog() }) {
                Text("Cancelar")
            }
        }
    )
}
