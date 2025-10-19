package cl.tuusuario.healing.ui.screens.viewmodels // Paquete centralizado y correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importa la entidad desde la ubicación correcta
import cl.tuusuario.healing.data.local.PersonalDataEntity
// ¡Importa el REPOSITORIO UNIFICADO desde la ubicación correcta!
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Datos Personales del paciente.
 * Se encarga de obtener los datos existentes y de guardarlos.
 */
class PersonalDataViewModel(
    private val repository: PatientDataRepository // Pide el repositorio unificado como dependencia
) : ViewModel() {

    /**
     * Expone los datos personales como un StateFlow.
     * La UI observará este flujo para mostrar siempre la información más reciente.
     * Si no hay datos en la base de datos, su valor será `null`.
     */
    val personalDataState: StateFlow<PersonalDataEntity?> = repository.getPersonalData()
        .stateIn(
            scope = viewModelScope,
            // El flujo se mantiene activo mientras la pantalla (UI) lo esté observando.
            // Se detiene 5 segundos después para ahorrar recursos.
            started = SharingStarted.WhileSubscribed(5000L),
            // El valor inicial mientras se espera la primera carga de la base de datos es null.
            initialValue = null
        )

    /**
     * Acción que la UI llamará para guardar (insertar o actualizar) los datos personales.
     * Usa `viewModelScope.launch` para ejecutar la operación de base de datos de forma segura
     * en un hilo secundario.
     */
    fun savePersonalData(name: String, birthDate: String, bloodType: String, allergies: String) {
        viewModelScope.launch {
            // Crea la entidad con los datos recibidos de la UI.
            // El 'id' se deja en su valor por defecto (0) para que Room sepa que es una inserción/actualización.
            val dataToSave = PersonalDataEntity(
                name = name,
                birthDate = birthDate,
                bloodType = bloodType,
                allergies = allergies
            )
            // Llama a la función 'suspend' del repositorio para guardar los datos.
            repository.upsertPersonalData(dataToSave)
        }
    }
}
