package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.PatientEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PatientDetailViewModel(
    private val repository: PatientDataRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val patientId: String = savedStateHandle.get<String>("patientId")!!

    private val _patientState = MutableStateFlow<PatientEntity?>(null)
    val patientState = _patientState.asStateFlow()

    var diagnosis by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            val patient = repository.getPatientById(patientId).firstOrNull()
            if (patient != null) {
                _patientState.value = patient
                diagnosis = patient.diagnosis
            }
        }
    }

    fun onDiagnosisChange(newDiagnosis: String) {
        diagnosis = newDiagnosis
    }

    fun savePatientDetails() {
        viewModelScope.launch {
            val currentPatient = _patientState.value
            if (currentPatient != null) {
                val updatedPatient = currentPatient.copy(diagnosis = diagnosis)
                repository.upsertPatient(updatedPatient)
            }
        }
    }
}
