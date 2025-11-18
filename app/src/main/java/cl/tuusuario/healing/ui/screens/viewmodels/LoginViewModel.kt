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

class LoginViewModel(private val repository: PatientDataRepository) : ViewModel() {

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
        emailError = if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            "Formato de email incorrecto"
        } else {
            null
        }
        loginError = null // Limpia el error al escribir
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        passwordError = if (newPassword.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
        loginError = null // Limpia el error al escribir
    }

    fun onLoginClick(asProfessional: Boolean) {
        if (!isFormValid) return

        viewModelScope.launch {
            val user = repository.findUserByEmail(email)

            if (user == null) {
                loginError = "Usuario no encontrado o contraseña incorrecta"
                return@launch
            }

            if (user.passwordHash == password) {
                if (asProfessional) {
                    _navigationEvent.send(NavigationEvent.NavigateToProfessionalHome)
                } else {
                    // ¡Aquí está el cambio! Enviamos el nombre del usuario.
                    _navigationEvent.send(NavigationEvent.NavigateToPatientHome(user.name))
                }
            } else {
                loginError = "Usuario no encontrado o contraseña incorrecta"
            }
        }
    }

    fun onGoToRegisterClick() {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.NavigateToRegister)
        }
    }

    fun onAdminLogin(password: String) {
        viewModelScope.launch {
            if (password == "admin123") { // Contraseña de administrador hardcoreada
                _navigationEvent.send(NavigationEvent.NavigateToAdminHome)
            } else {
                loginError = "Contraseña de administrador incorrecta"
            }
        }
    }

    // --- CLASE PARA EVENTOS DE NAVEGACIÓN ---
    sealed class NavigationEvent {
        data class NavigateToPatientHome(val userName: String) : NavigationEvent()
        object NavigateToProfessionalHome : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
        object NavigateToAdminHome : NavigationEvent()

    }
}
