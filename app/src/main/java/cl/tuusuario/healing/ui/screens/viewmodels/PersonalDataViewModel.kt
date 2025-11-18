package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.PersonalDataEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonalDataViewModel(
    private val repository: PatientDataRepository
) : ViewModel() {

    // --- UI STATE ---
    var name by mutableStateOf("")
        private set
    var birthDate by mutableStateOf("")
        private set
    var bloodType by mutableStateOf("")
        private set
    var allergies by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    val isFormValid: Boolean
        get() = nameError == null && name.isNotBlank()

    val personalDataState: StateFlow<PersonalDataEntity?> = repository.getPersonalData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            personalDataState.filterNotNull().collect { data ->
                name = data.name
                birthDate = data.birthDate
                bloodType = data.bloodType
                allergies = data.allergies
            }
        }
    }

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

    fun onBirthDateChange(newBirthDate: String) {
        birthDate = newBirthDate
    }

    fun onBloodTypeChange(newBloodType: String) {
        bloodType = newBloodType
    }

    fun onAllergiesChange(newAllergies: String) {
        allergies = newAllergies
    }

    fun savePersonalData() {
        if (!isFormValid) {
            return
        }
        viewModelScope.launch {
            val dataToSave = PersonalDataEntity(
                name = name,
                birthDate = birthDate,
                bloodType = bloodType,
                allergies = allergies
            )
            repository.upsertPersonalData(dataToSave)
        }
    }
}
