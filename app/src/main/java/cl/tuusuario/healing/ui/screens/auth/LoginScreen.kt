package cl.tuusuario.healing.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginAsPatient: () -> Unit,
    onLoginAsProfessional: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val isValid = email.contains("@") && pass.length >= 6

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Healing", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("correo electrónico") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = pass, onValueChange = { pass = it },
                label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onGoToRegister) { Text("¿No tienes cuenta? Regístrate") }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onLoginAsPatient, enabled = isValid, modifier = Modifier.fillMaxWidth()) {
                Text("Ingresar (Paciente)")
            }
            OutlinedButton(onClick = onLoginAsProfessional, enabled = isValid, modifier = Modifier.fillMaxWidth()) {
                Text("Ingresar (Profesional)")
            }
        }
    }
}
