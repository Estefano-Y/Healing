package cl.tuusuario.healing.ui.screens.viewmodels



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.MedsReminderEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Recordatorios de Medicamentos.
 */
class MedsReminderViewModel(
    private val repository: PatientDataRepository
) : ViewModel() {

    /**
     * Expone la LISTA de recordatorios como un StateFlow.
     * La UI observará este flujo para mostrar siempre los datos más recientes.
     */
    val remindersState: StateFlow<List<MedsReminderEntity>> = repository.getAllMedsReminders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList() // El valor inicial es una lista vacía
        )

    /**
     * Acción para guardar un NUEVO recordatorio.
     */
    fun addReminder(medName: String, dose: String, time: String) {
        // Validación para no guardar recordatorios vacíos
        if (medName.isBlank() || dose.isBlank() || time.isBlank()) return

        viewModelScope.launch {
            val newReminder = MedsReminderEntity(
                medName = medName,
                dose = dose,
                time = time,
                isTaken = false // Por defecto, una nueva toma no ha sido realizada
            )
            repository.upsertMedsReminder(newReminder)
        }
    }

    /**
     * Acción para eliminar un recordatorio existente usando su ID.
     */
    fun deleteReminder(reminderId: Int) {
        viewModelScope.launch {
            repository.deleteMedsReminder(reminderId)
        }
    }

    /**
     * Acción para marcar un recordatorio como "tomado" o "no tomado".
     */
    fun toggleIsTaken(reminder: MedsReminderEntity) {
        viewModelScope.launch {
            // Creamos una copia del recordatorio con el estado 'isTaken' invertido
            val updatedReminder = reminder.copy(isTaken = !reminder.isTaken)
            repository.upsertMedsReminder(updatedReminder)
        }
    }
}
