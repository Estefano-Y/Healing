package cl.tuusuario.healing.ui.screens.viewmodels // Ruta correcta según tu imagen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// ¡Usa la entidad de tu archivo DataBase.kt!
import cl.tuusuario.healing.data.local.EmergencyContactEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Contacto de Emergencia, adaptado a tu código.
 */
class EmergencyContactViewModel(
    private val repository: PatientDataRepository
) : ViewModel() {

    /**
     * Expone el ÚNICO contacto de emergencia como un StateFlow.
     * Su valor será `null` si no hay ningún contacto guardado.
     */
    val emergencyContactState: StateFlow<EmergencyContactEntity?> = repository.getEmergencyContact()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    /**
     * Acción para guardar (insertar o actualizar) el contacto de emergencia.
     */
    fun saveEmergencyContact(name: String, relationship: String, phone: String) {
        if (name.isBlank() || phone.isBlank()) return // Validación simple

        viewModelScope.launch {
            // Usa el id fijo '1' como lo definiste en tu entidad.
            val contactToSave = EmergencyContactEntity(
                id = 1,
                name = name,
                relationship = relationship,
                phone = phone
            )
            repository.upsertEmergencyContact(contactToSave)
        }
    }
}
