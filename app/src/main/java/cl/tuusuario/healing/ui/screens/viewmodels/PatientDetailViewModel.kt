package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.PatientEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de detalle de un paciente específico,
 * vista por el profesional.
 */
class PatientDetailViewModel(
    private val repository: PatientDataRepository,
    // SavedStateHandle nos permite leer argumentos pasados a la pantalla, como el ID del paciente.
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Obtenemos el ID del paciente que se pasó como argumento de navegación.
    // "patientId" debe ser el nombre exacto del argumento que definiremos en la ruta de navegación.
    private val patientId: String = savedStateHandle.get<String>("patientId")!!

    // Flujo privado para almacenar los datos del paciente.
    private val _patientState = MutableStateFlow<PatientEntity?>(null)
    // Flujo público y de solo lectura para que la UI lo observe.
    val patientState = _patientState.asStateFlow()

    init {
        // Al iniciar el ViewModel, cargamos los datos del paciente usando el ID.
        viewModelScope.launch {
            repository.getPatientById(patientId).collect { patient ->
                _patientState.value = patient
            }
        }
    }
}
