package cl.tuusuario.healing.ui.screens.viewmodels // Paquete centralizado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.Note
// --- ¡IMPORT CORRECTO DEL REPOSITORIO! ---
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel para la pantalla de Notas.
 * Expone el estado de las notas y las acciones para modificarlas.
 */
class NotesViewModel(
    private val repository: PatientDataRepository // Pide el repositorio unificado
) : ViewModel() {

    // Expone la lista de todas las notas como un StateFlow que la UI puede consumir.
    val notesState: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            // Inicia el flujo cuando la UI está visible y lo detiene 5 segundos después.
            started = SharingStarted.WhileSubscribed(5000L),
            // El valor inicial mientras se cargan los datos es una lista vacía.
            initialValue = emptyList()
        )

    // Función para crear o actualizar una nota.
    // No necesita viewModelScope.launch porque la función del DAO no es 'suspend'.
    // Room ya lo ejecuta en un hilo de fondo.
    fun upsertNote(note: Note) {
        repository.upsertNote(note)
    }

    // Función para borrar una nota.
    fun deleteNote(note: Note) {
        repository.deleteNote(note)
    }
}
