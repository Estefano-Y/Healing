package cl.tuusuario.healing.ui.screens.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // --- Estados del formulario ---
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    // --- Estados de los errores ---
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var loginError by mutableStateOf<String?>(null) // Para errores generales de login
        private set

    // --- Estado de validación del formulario ---
    var isFormValid by mutableStateOf(false)
        private set

    // --- Canal para eventos de navegación ---
    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // --- Funciones para actualizar campos ---
    fun onEmailChange(newEmail: String) {
        email = newEmail
        loginError = null // Limpiar error general al escribir
        validateEmail()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        loginError = null // Limpiar error general al escribir
        validatePassword()
    }

    // --- Lógica de validación ---
    private fun validateEmail() {
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Formato de correo inválido"
        } else {
            null
        }
        validateForm()
    }

    private fun validatePassword() {
        passwordError = if (password.isBlank()) "La contraseña es obligatoria" else null
        validateForm()
    }

    private fun validateForm() {
        isFormValid = emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()
    }

    // --- Lógica de Login ---
    fun onLoginClick(asProfessional: Boolean) {
        if (!isFormValid) {
            loginError = "Por favor, corrige los errores antes de continuar."
            return
        }

        // --- SIMULACIÓN DE LOGIN ---
        // Aquí iría tu lógica real para autenticar con una API o base de datos.
        // Por ahora, simularemos un login exitoso si la contraseña es "123456".
        viewModelScope.launch {
            if (password == "123456") {
                if (asProfessional) {
                    _navigationEvent.send(NavigationEvent.NavigateToProfessionalHome)
                } else {
                    _navigationEvent.send(NavigationEvent.NavigateToPatientHome)
                }
            } else {
                loginError = "Email o contraseña incorrectos."
            }
        }
    }

    fun onGoToRegisterClick() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateToRegister)
        }
    }

    // --- Clase sellada para eventos ---
    sealed class NavigationEvent {
        object NavigateToPatientHome : NavigationEvent()
        object NavigateToProfessionalHome : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
    }
}
