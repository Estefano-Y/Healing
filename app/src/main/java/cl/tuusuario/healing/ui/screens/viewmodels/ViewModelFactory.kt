package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.tuusuario.healing.data.local.repository.PatientDataRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: PatientDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NotesViewModel::class.java) -> {
                NotesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PersonalDataViewModel::class.java) -> {
                PersonalDataViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EmergencyContactViewModel::class.java) -> {
                EmergencyContactViewModel(repository) as T
            }

            // --- ¡AÑADE ESTE NUEVO BLOQUE! ---
            modelClass.isAssignableFrom(MedsReminderViewModel::class.java) -> {
                MedsReminderViewModel(repository) as T
            }
            // --- FIN DEL BLOQUE AÑADIDO ---

            else -> throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }
}
