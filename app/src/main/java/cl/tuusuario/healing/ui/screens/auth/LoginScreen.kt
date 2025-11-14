package cl.tuusuario.healing.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.LoginViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginAsPatient: (userName: String) -> Unit,
    onLoginAsProfessional: () -> Unit,
    onGoToRegister: () -> Unit,
    onLoginAsAdmin: () -> Unit
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

    val viewModel: LoginViewModel = viewModel(factory = ViewModelFactory(repository))
    var visible by remember { mutableStateOf(false) }
    var showAdminLogin by remember { mutableStateOf(false) }
    var adminPassword by remember { mutableStateOf("") }


    LaunchedEffect(key1 = Unit) {
        visible = true
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is LoginViewModel.NavigationEvent.NavigateToPatientHome -> onLoginAsPatient(event.userName)
                is LoginViewModel.NavigationEvent.NavigateToProfessionalHome -> onLoginAsProfessional()
                is LoginViewModel.NavigationEvent.NavigateToRegister -> onGoToRegister()
                is LoginViewModel.NavigationEvent.NavigateToAdminHome -> onLoginAsAdmin()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
        ) {
            var tapCount by remember { mutableStateOf(0) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // --- Animated Logo ---
                val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, delayMillis = 200),
                        repeatMode = RepeatMode.Reverse
                    ), label = "scale"
                )
                val rotation by infiniteTransition.animateFloat(
                    initialValue = -10f,
                    targetValue = 10f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse
                    ), label = "rotation"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .scale(scale)
                        .rotate(rotation)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Logo de Healing",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(90.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Bienvenido a Healing",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { 
                                tapCount++
                                if (tapCount >= 3) {
                                    showAdminLogin = true
                                }
                            }
                        )
                    }
                )
                Text(
                    text = "Tu asistente de salud personal",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Resto del contenido...
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Correo Electrónico") },
                    isError = viewModel.emailError != null || viewModel.loginError != null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    isError = viewModel.passwordError != null || viewModel.loginError != null,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.loginError != null) {
                    Text(
                        text = viewModel.loginError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.onLoginClick(asProfessional = false) },
                    enabled = viewModel.isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Ingresar como Paciente", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.onLoginClick(asProfessional = true) },
                    enabled = viewModel.isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Soy un Profesional", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿No tienes una cuenta? Regístrate aquí",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { viewModel.onGoToRegisterClick() }
                )
            }
        }
    }
    if (showAdminLogin) {
        AlertDialog(
            onDismissRequest = { showAdminLogin = false },
            title = { Text("Acceso de Administrador") },
            text = {
                OutlinedTextField(
                    value = adminPassword,
                    onValueChange = { adminPassword = it },
                    label = { Text("Contraseña de Administrador") },
                    visualTransformation = PasswordVisualTransformation()
                )
            },
            confirmButton = {
                Button(onClick = { 
                    viewModel.onAdminLogin(adminPassword)
                     showAdminLogin = false
                 }) {
                    Text("Ingresar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminLogin = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
