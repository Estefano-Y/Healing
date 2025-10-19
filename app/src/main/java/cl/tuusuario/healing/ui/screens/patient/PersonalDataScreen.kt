package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    onBack: () -> Unit,
    viewModel: PersonalDataViewModel = viewModel() // Inyecta el ViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Datos Personales") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Para que el formulario sea desplazable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Nombre
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Apellido
            OutlinedTextField(
                value = viewModel.lastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo RUT
            OutlinedTextField(
                value = viewModel.rut,
                onValueChange = { viewModel.onRutChange(it) },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Fecha de Nacimiento
            OutlinedTextField(
                value = viewModel.birthDate,
                onValueChange = { viewModel.onBirthDateChange(it) },
                label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Dirección
            OutlinedTextField(
                value = viewModel.address,
                onValueChange = { viewModel.onAddressChange(it) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Teléfono
            OutlinedTextField(
                value = viewModel.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Botón para guardar
            Button(
                onClick = {
                    viewModel.onSaveData()
                    // Opcional: Mostrar un mensaje o navegar hacia atrás después de guardar
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR CAMBIOS")
            }
        }
    }
}
