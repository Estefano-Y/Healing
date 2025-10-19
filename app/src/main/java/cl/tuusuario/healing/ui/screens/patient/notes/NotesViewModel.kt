// El 'package' es correcto gracias al movimiento de la carpeta
package cl.tuusuario.healing.ui.screens.patient.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// --- IMPORTS DEFINITIVOS ---
import cl.tuusuario.healing.data.local.Note // <-- Importa la clase de datos 'Note'
import cl.tuusuario.healing.data.local.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// El UiState ahora usa la clase 'Note' correcta
data class NotesUiState(
    val notes: List<Note> = emptyList()
)

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private var lastDeletedNote: Note? = null

    init {
        viewModelScope.launch {
            // Esto ahora funciona porque el tipo que viene de 'getAllNotes' (List<Note>)
            // coincide con el que espera 'UiState'.
            repository.getAllNotes().collect { notesFromDb ->
                _uiState.update { it.copy(notes = notesFromDb) }
            }
        }
    }

    // CÓDIGO CORREGIDO
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            // Creamos una instancia de la clase 'Note' de la base de datos
            repository.addNote(
                Note(
                    title = title,
                    content = content,
                    // Añadimos el timestamp actual en milisegundos
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // CÓDIGO CORREGIDO
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            lastDeletedNote = note
            // El DAO espera un 'Long', así que convertimos el 'id' a Long.
            repository.deleteNoteById(note.id.toLong()) // <-- ¡Solución!
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            lastDeletedNote?.let { noteToRestore ->
                repository.addNote(noteToRestore)
                lastDeletedNote = null
            }
        }
    }
}
