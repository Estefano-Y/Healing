package cl.tuusuario.healing.ui.screens.viewmodels // Asegúrate que el paquete es el correcto

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Login.
 * Maneja el estado de los campos, la validación y los eventos de navegación.
 */
class LoginViewModel : ViewModel() {

    // --- ESTADO DE LA UI ---
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var loginError by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && emailError == null && passwordError == null

    // --- EVENTOS DE NAVEGACIÓN ---
    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // --- ACCIONES DESDE LA UI ---
    fun onEmailChange(newEmail: String) {
        email = newEmail
        // Validación simple
        emailError = if (!newEmail.contains("@")) "Email no válido" else null
        loginError = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        // Validación simple
        passwordError = if (newPassword.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
        loginError = null
    }

    fun onLoginClick(asProfessional: Boolean) {
        if (!isFormValid) {
            loginError = "Por favor, corrige los errores en el formulario."
            return
        }

        // Aquí iría tu lógica de autenticación (con Firebase, una API, etc.)
        // Por ahora, simulamos una navegación exitosa.
        viewModelScope.launch {
            if (asProfessional) {
                _navigationEvent.send(NavigationEvent.NavigateToProfessionalHome)
            } else {
                _navigationEvent.send(NavigationEvent.NavigateToPatientHome)
            }
        }
    }

    fun onGoToRegisterClick() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateToRegister)
        }
    }

    // --- CLASES PARA EVENTOS DE NAVEGACIÓN ---
    sealed class NavigationEvent {
        object NavigateToPatientHome : NavigationEvent()
        object NavigateToProfessionalHome : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
    }
}
