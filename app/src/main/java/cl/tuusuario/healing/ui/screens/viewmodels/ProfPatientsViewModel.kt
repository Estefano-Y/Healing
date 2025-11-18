package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.PatientEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfPatientsViewModel(
    private val repository: PatientDataRepository
) : ViewModel() {

    private val professionalId = "dr.house@example.com"

    private val _patientsState = MutableStateFlow<List<PatientEntity>>(emptyList())
    val patientsState = _patientsState.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            repository.addDummyProfessionalData()
            loadPatients()
        }
    }

    private suspend fun loadPatients() {
        _isLoading.value = true
        repository.getPatientsForProfessional(professionalId).collect { patientsList ->
            _patientsState.value = patientsList
            _isLoading.value = false
        }
    }

    // --- ¡NUEVA FUNCIÓN AÑADIDA! ---
    /**
     * Crea y guarda un nuevo paciente en la base de datos.
     */
    fun addPatient(name: String, age: Int, diagnosis: String) {
        viewModelScope.launch {
            val newPatientId = "p${System.currentTimeMillis()}"
            val newPatient = PatientEntity(
                id = newPatientId,
                name = name,
                age = age,
                diagnosis = diagnosis,
                lastActivity = "Recién añadido",
                hasCriticalAlert = false,
                professionalOwnerId = professionalId
            )
            repository.upsertPatient(newPatient)
        }
    }
}
