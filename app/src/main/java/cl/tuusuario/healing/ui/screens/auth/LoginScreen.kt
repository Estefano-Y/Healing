package cl.tuusuario.healing.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.password
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
// ¡EL ÚNICO IMPORT DE VIEWMODEL QUE NECESITAS!
import cl.tuusuario.healing.ui.screens.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginAsPatient: () -> Unit,
    onLoginAsProfessional: () -> Unit,
    onGoToRegister: () -> Unit
) {
    // Se instancia el ViewModel desde la carpeta correcta.
    val viewModel: LoginViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginViewModel.NavigationEvent.NavigateToPatientHome -> onLoginAsPatient()
                is LoginViewModel.NavigationEvent.NavigateToProfessionalHome -> onLoginAsProfessional()
                is LoginViewModel.NavigationEvent.NavigateToRegister -> onGoToRegister()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido a Healing", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // --- CAMPO EMAIL ---
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") }, // La sintaxis simple es la correcta
                isError = viewModel.emailError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.emailError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // --- CAMPO CONTRASEÑA ---
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
            Spacer(modifier = Modifier.height(24.dp))

            // --- Mensaje de error general ---
            viewModel.loginError?.let { error ->
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- Botones de Login ---
            Button(
                onClick = { viewModel.onLoginClick(asProfessional = false) },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar como Paciente")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { viewModel.onLoginClick(asProfessional = true) },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar como Profesional")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Enlace a Registro ---
            Text(
                text = "¿No tienes una cuenta? Regístrate aquí",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { viewModel.onGoToRegisterClick() }
            )
        }
    }
}
