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

class RegisterViewModel : ViewModel() {

    // ... (todos los 'var' de estado del formulario se mantienen igual) ...
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
    var isFormValid by mutableStateOf(false)
        private set

    // --- NUEVO: Canal para eventos de navegación ---
    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // ... (todas las funciones 'on...Change' y 'validate...' se mantienen igual) ...
    fun onNameChange(newName: String) {
        name = newName
        validateName()
    }
    fun onEmailChange(newEmail: String) {
        email = newEmail
        validateEmail()
    }
    fun onPasswordChange(newPassword: String) {
        password = newPassword
        validatePassword()
    }
    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        validateConfirmPassword()
    }
    private fun validateName() {
        nameError = if (name.isBlank()) "El nombre es obligatorio" else null
        validateForm()
    }
    private fun validateEmail() {
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Formato de correo inválido"
        } else {
            null
        }
        validateForm()
    }
    private fun validatePassword() {
        passwordError = if (password.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
        validateConfirmPassword() // Validar la confirmación si la contraseña principal cambia
        validateForm()
    }
    private fun validateConfirmPassword() {
        confirmPasswordError = if (password != confirmPassword) "Las contraseñas no coinciden" else null
        validateForm()
    }
    private fun validateForm() {
        isFormValid = nameError == null && emailError == null && passwordError == null && confirmPasswordError == null && name.isNotBlank() && email.isNotBlank() && password.isNotBlank()
    }


    fun onRegisterClick() {
        validateName()
        validateEmail()
        validatePassword()
        validateConfirmPassword()

        if (isFormValid) {
            println("Formulario Válido: Registrando a $name con email $email")
            // TODO: Iniciar el proceso de registro real (API, etc.)

            // --- NUEVO: Enviar evento para navegar hacia atrás ---
            viewModelScope.launch {
                _navigationEvent.send(NavigationEvent.NavigateBack)
            }
        } else {
            println("Formulario Inválido. Por favor, revisa los errores.")
        }
    }

    // --- NUEVO: Clase sellada para los eventos de navegación ---
    sealed class NavigationEvent {
        object NavigateBack : NavigationEvent()
    }
}
