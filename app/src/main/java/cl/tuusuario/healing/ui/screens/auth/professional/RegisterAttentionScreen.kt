package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Modelo para los pacientes que se mostrarán en el desplegable
private data class PatientSelectItem(val id: String, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterAttentionScreen(onBack: () -> Unit) {

    // --- Datos de ejemplo que vendrían de un ViewModel ---
    val patients = remember {
        listOf(
            PatientSelectItem("1", "Juan Pérez González"),
            PatientSelectItem("2", "Ana María López"),
            PatientSelectItem("3", "Carlos Soto Martínez"),
            PatientSelectItem("4", "Luisa Fernández Díaz")
        )
    }

    // --- Estados del formulario ---
    var selectedPatient by remember { mutableStateOf<PatientSelectItem?>(null) }
    var attentionDate by remember { mutableStateOf("") }
    var attentionTime by remember { mutableStateOf("") }
    var attentionType by remember { mutableStateOf("") }
    var consultationNotes by remember { mutableStateOf("") }
    var indications by remember { mutableStateOf("") }

    // Estado para el menú desplegable de pacientes
    var isPatientMenuExpanded by remember { mutableStateOf(false) }

    // El botón de guardar solo se activa si los campos mínimos están llenos
    val isFormValid = selectedPatient != null && attentionDate.isNotBlank() && consultationNotes.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nueva Atención") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // Barra inferior para el botón de acción principal
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = { /* Lógica para guardar la atención */ },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Guardar Atención")
                }
            }
        }
    ) { paddingValues ->
        // Usamos Column con scroll para que el formulario no se corte en pantallas pequeñas
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- Selector de Paciente (Menú Desplegable) ---
            ExposedDropdownMenuBox(
                expanded = isPatientMenuExpanded,
                onExpandedChange = { isPatientMenuExpanded = !isPatientMenuExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(), // Ancla el menú al TextField
                    readOnly = true,
                    value = selectedPatient?.name ?: "Seleccionar Paciente",
                    onValueChange = {},
                    label = { Text("Paciente *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPatientMenuExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = isPatientMenuExpanded,
                    onDismissRequest = { isPatientMenuExpanded = false }
                ) {
                    patients.forEach { patient ->
                        DropdownMenuItem(
                            text = { Text(patient.name) },
                            onClick = {
                                selectedPatient = patient
                                isPatientMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // --- Fila para Fecha y Hora ---
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = attentionDate,
                    onValueChange = { attentionDate = it },
                    label = { Text("Fecha *") },
                    placeholder = { Text("DD/MM/AAAA") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Seleccionar fecha",
                            Modifier.clickable { /* Abrir DatePickerDialog */ }
                        )
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = attentionTime,
                    onValueChange = { attentionTime = it },
                    label = { Text("Hora") },
                    placeholder = { Text("HH:MM") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Seleccionar hora",
                            Modifier.clickable { /* Abrir TimePickerDialog */ }
                        )
                    }
                )
            }

            // --- Tipo de Atención ---
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = attentionType,
                onValueChange = { attentionType = it },
                label = { Text("Tipo de Atención") },
                placeholder = { Text("Ej: Control, Examen, Urgencia...") }
            )

            // --- Notas de la Consulta ---
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                value = consultationNotes,
                onValueChange = { consultationNotes = it },
                label = { Text("Notas de la Consulta *") },
                placeholder = { Text("Describa los síntomas, diagnóstico, etc.") },
                maxLines = 10
            )

            // --- Indicaciones para el Paciente ---
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = indications,
                onValueChange = { indications = it },
                label = { Text("Indicaciones / Plan") },
                placeholder = { Text("Pasos a seguir por el paciente...") },
                maxLines = 5
            )
        }
    }
}
