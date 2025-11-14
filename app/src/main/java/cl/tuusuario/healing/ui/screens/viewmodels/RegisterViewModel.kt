package cl.tuusuario.healing.ui.screens.viewmodels

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Registro.
 * Maneja el estado de los campos, la validación y los eventos de navegación.
 */
class RegisterViewModel(private val repository: PatientDataRepository) : ViewModel() {

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

    var registrationError by mutableStateOf<String?>(null)
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
        if (newName.isBlank()) {
            nameError = "El nombre no puede estar vacío."
        } else if (!newName.all { it.isLetter() || it.isWhitespace() }) {
            nameError = "El nombre solo puede contener letras y espacios."
        } else {
            nameError = null
        }
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            "Formato de email incorrecto"
        } else {
            null
        }
        registrationError = null // Limpiamos el error al cambiar el email
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

        viewModelScope.launch {
            val success = repository.registerUser(name, email, password)
            if (success) {
                _navigationEvent.send(NavigationEvent.NavigateBack)
            } else {
                emailError = "El email ingresado ya está en uso"
                registrationError = "El email ingresado ya está en uso"
            }
        }
    }

    // --- CLASE PARA EVENTOS DE NAVEGACIÓN ---
    sealed class NavigationEvent {
        object NavigateBack : NavigationEvent()
    }
}
