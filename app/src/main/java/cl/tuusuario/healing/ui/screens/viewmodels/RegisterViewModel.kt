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
 * ViewModel para la pantalla de Registro.
 * Maneja el estado de los campos, la validación y los eventos de navegación.
 */
class RegisterViewModel : ViewModel() {

    // --- ESTADO DE LA UI ---
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() &&
                confirmPassword.isNotBlank() && nameError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null

    // --- EVENTOS DE NAVEGACIÓN ---
    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // --- ACCIONES DESDE LA UI ---
    fun onNameChange(newName: String) {
        name = newName
        nameError = if (newName.isBlank()) "El nombre no puede estar vacío" else null
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = if (!newEmail.contains("@")) "Email no válido" else null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        passwordError = if (newPassword.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
        validateConfirmPassword()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        validateConfirmPassword()
    }

    private fun validateConfirmPassword() {
        confirmPasswordError = if (password != confirmPassword) "Las contraseñas no coinciden" else null
    }

    fun onRegisterClick() {
        if (!isFormValid) return

        // Aquí iría tu lógica de registro (Firebase, API, etc.)
        // Por ahora, simulamos una navegación hacia atrás exitosa.
        viewModelScope.launch {
            // Podrías enviar un evento a la UI para mostrar un Toast/Snackbar de "Registro exitoso"
            _navigationEvent.send(NavigationEvent.NavigateBack)
        }
    }

    // --- CLASE PARA EVENTOS DE NAVEGACIÓN ---
    sealed class NavigationEvent {
        object NavigateBack : NavigationEvent()
    }
}
