package cl.tuusuario.healing.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // --- ASEGÚRATE DE QUE ESTE MÉTODO EXISTA ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note) // 'suspend' es importante para corrutinas

    // ... otros métodos como 'getAllNotes' y 'deleteNoteById'
    @Query("SELECT * FROM notes_table ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM notes_table WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)
}
