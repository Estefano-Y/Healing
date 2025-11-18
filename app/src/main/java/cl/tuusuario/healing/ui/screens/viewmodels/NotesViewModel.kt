package cl.tuusuario.healing.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.Note
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Notas.
 * Expone el estado de las notas y las acciones para modificarlas.
 */
class NotesViewModel(
    private val repository: PatientDataRepository
) : ViewModel() {

    val notesState: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // --- ¡CORRECCIÓN! ---
    // Ambas acciones ahora se ejecutan dentro de un viewModelScope.launch
    // para asegurar que las operaciones de base de datos se hagan en un hilo secundario.

    fun upsertNote(note: Note) {
        viewModelScope.launch {
            repository.upsertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
