package cl.tuusuario.healing.data.local.repository

import cl.tuusuario.healing.data.local.Note
import cl.tuusuario.healing.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao) {

    // Esta función obtiene todas las notas. Ya debería estar funcionando.
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    // --- ¡MÉTODO CORREGIDO/AÑADIDO! ---
    // Esta es la función que tu ViewModel está buscando.
    // Llama al método 'insert' del DAO.
    suspend fun addNote(note: Note) {
        noteDao.insert(note) // Asegúrate de que tu DAO tiene un método 'insert'
    }

    // --- ¡MÉTODO CORREGIDO/AÑADIDO! ---
    // Esta función elimina una nota por su ID.
    suspend fun deleteNoteById(noteId: Long) {
        noteDao.deleteNoteById(noteId)
    }
}
