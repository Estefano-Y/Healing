package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import cl.tuusuario.healing.data.local.repository.PatientDataRepository

/**
 * ViewModelFactory actualizada para poder crear ViewModels con y sin SavedStateHandle.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: PatientDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()

        return when {
            // ViewModels que NO necesitan SavedStateHandle
            modelClass.isAssignableFrom(NotesViewModel::class.java) ->
                NotesViewModel(repository) as T
            modelClass.isAssignableFrom(PersonalDataViewModel::class.java) ->
                PersonalDataViewModel(repository) as T
            modelClass.isAssignableFrom(EmergencyContactViewModel::class.java) ->
                EmergencyContactViewModel(repository) as T
            modelClass.isAssignableFrom(MedsReminderViewModel::class.java) ->
                MedsReminderViewModel(repository) as T
            modelClass.isAssignableFrom(ProfPatientsViewModel::class.java) ->
                ProfPatientsViewModel(repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(repository) as T
            modelClass.isAssignableFrom(AdminViewModel::class.java) ->
                AdminViewModel(repository) as T

            // --- ¡NUEVO! Añadimos el PatientHomeViewModel a la factory ---
            modelClass.isAssignableFrom(PatientHomeViewModel::class.java) ->
                PatientHomeViewModel(repository) as T

            modelClass.isAssignableFrom(PatientDetailViewModel::class.java) ->
                PatientDetailViewModel(repository, savedStateHandle) as T

            modelClass.isAssignableFrom(DrugInfoViewModel::class.java) ->
                DrugInfoViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }
}
