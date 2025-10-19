package cl.tuusuario.healing.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit // Parámetro para manejar la navegación hacia atrás
) {
    // 1. Instanciamos el ViewModel
    val viewModel: RegisterViewModel = viewModel()

    // 2. Escuchamos eventos de navegación del ViewModel
    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RegisterViewModel.NavigationEvent.NavigateBack -> {
                    onNavigateBack() // Ejecutamos la acción de navegación
                }
            }
        }
    }

    // Usamos Surface para el fondo
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Para que sea scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // --- Campo Nombre ---
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre completo") },
                isError = viewModel.nameError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.nameError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo Email ---
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                isError = viewModel.emailError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.emailError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo Contraseña ---
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                isError = viewModel.passwordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.passwordError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo Confirmar Contraseña ---
            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirmar contraseña") },
                isError = viewModel.confirmPasswordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.confirmPasswordError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Botón de Registro ---
            Button(
                onClick = { viewModel.onRegisterClick() },
                enabled = viewModel.isFormValid, // El botón se activa solo si el formulario es válido
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}
