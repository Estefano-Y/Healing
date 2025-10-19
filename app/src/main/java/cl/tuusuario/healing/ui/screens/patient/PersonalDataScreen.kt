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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    onBack: () -> Unit
) {
    // --- Estados para gestionar los datos y el modo de edición ---
    var isInEditMode by remember { mutableStateOf(false) }

    // Estados para cada campo del formulario. En una app real, vendrían de un ViewModel.
    var name by remember { mutableStateOf("Juan Pérez") }
    var birthDate by remember { mutableStateOf("25/09/1990") }
    var bloodType by remember { mutableStateOf("O+") }
    var phone by remember { mutableStateOf("+56 9 1234 5678") }
    var email by remember { mutableStateOf("juan.perez@email.com") }
    var allergies by remember { mutableStateOf("Alergia al maní") }
    var observations by remember { mutableStateOf("Cirugía de apendicitis en 2015.") }

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
                    // Si estamos en modo edición, "guardamos" y cambiamos de modo.
                    // Si no, solo cambiamos a modo edición.
                    isInEditMode = !isInEditMode
                }
            ) {
                // El icono del FAB cambia con una animación suave
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Para que el formulario sea desplazable
        ) {
            Spacer(Modifier.height(16.dp))

            // --- Sección de Información Básica ---
            SectionTitle("Información Básica")
            // Campo Nombre
            EditableDataField(
                label = "Nombre Completo",
                value = name,
                onValueChange = { name = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            // Campo Fecha de Nacimiento (usamos teclado numérico como ejemplo)
            EditableDataField(
                label = "Fecha de Nacimiento",
                value = birthDate,
                onValueChange = { birthDate = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                keyboardType = KeyboardType.Number
            )
            // Campo Tipo de Sangre
            EditableDataField(
                label = "Tipo de Sangre",
                value = bloodType,
                onValueChange = { bloodType = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Bloodtype, contentDescription = null) }
            )

            Spacer(Modifier.height(24.dp))

            // --- Sección de Contacto ---
            SectionTitle("Información de Contacto")
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

            // --- Sección de Datos Médicos ---
            SectionTitle("Datos Médicos Adicionales")
            EditableDataField(
                label = "Alergias Conocidas",
                value = allergies,
                onValueChange = { allergies = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.MedicalInformation, contentDescription = null) }
            )
            EditableDataField(
                label = "Observaciones Médicas",
                value = observations,
                onValueChange = { observations = it },
                editMode = isInEditMode,
                leadingIcon = { Icon(Icons.Default.Note, contentDescription = null) },
                singleLine = false // Permite múltiples líneas
            )

            Spacer(Modifier.height(80.dp)) // Espacio para que el FAB no tape el último campo
        }
    }
}

// Composable para los títulos de cada sección
@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

// El componente clave: un campo que puede ser texto o un TextField
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
    // AnimatedContent proporciona una animación suave al cambiar entre los modos
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
            // Modo "Vista": mostramos el texto en un Row con el icono
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // El icono se muestra en un color más suave
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    leadingIcon()
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    Text(value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 2.dp))
                }
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }
    }
}
