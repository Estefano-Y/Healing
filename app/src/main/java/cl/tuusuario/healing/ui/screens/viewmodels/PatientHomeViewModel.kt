package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.MedsReminderEntity
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PatientHomeViewModel(repository: PatientDataRepository) : ViewModel() {

    // Este flow contendrá el recordatorio más próximo y relevante.
    val nextUpcomingReminder: StateFlow<MedsReminderEntity?> = repository.getUntakenReminders()
        .map { reminders ->
            // Obtenemos la hora actual
            val now = LocalTime.now()
            // Buscamos el primer recordatorio cuya hora sea posterior a la actual.
            // La lista ya viene ordenada por hora desde la consulta del DAO.
            reminders.find { reminder ->
                try {
                    LocalTime.parse(reminder.time, DateTimeFormatter.ofPattern("HH:mm"))
                        .isAfter(now)
                } catch (e: Exception) {
                    // Ignoramos los recordatorios con formato de hora incorrecto
                    false
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null // El valor inicial es nulo hasta que se cargue
        )
}
