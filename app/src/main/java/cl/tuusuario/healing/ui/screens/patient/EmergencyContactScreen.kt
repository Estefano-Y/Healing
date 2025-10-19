package cl.tuusuario.healing.ui.screens.patient
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons // <-- Import principal de Icons
import androidx.compose.material.icons.filled.* // <-- Import para los iconos específicos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
    onBack: () -> Unit
) {
    // --- Estados para el contacto y el modo edición ---
    var isInEditMode by remember { mutableStateOf(false) }

    // Datos de ejemplo. En una app real, vendrían de un ViewModel/base de datos.
    var contactName by remember { mutableStateOf("Ana González") }
    var relationship by remember { mutableStateOf("Madre") }
    var phoneNumber by remember { mutableStateOf("+56912345678") }

    // El contexto es necesario para lanzar intents (llamadas, mensajes)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacto de Emergencia") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isInEditMode = !isInEditMode }) {
                Crossfade(targetState = isInEditMode, label = "fab_icon") { isEditing ->
                    if (isEditing) {
                        Icon(Icons.Default.Done, contentDescription = "Guardar Contacto")
                    } else {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Contacto")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Usamos AnimatedVisibility para mostrar la tarjeta o el formulario de edición
            AnimatedVisibility(visible = !isInEditMode) {
                ViewContactCard(
                    name = contactName,
                    relationship = relationship,
                    phone = phoneNumber,
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                        context.startActivity(intent)
                    },
                    onMessage = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNumber"))
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
        }
    }
}

// Tarjeta que muestra el contacto y los botones de acción
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

// Formulario para editar la información del contacto
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

// Botón de acción reutilizable para "Llamar" y "Mensaje"
@Composable
private fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
