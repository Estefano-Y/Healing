package cl.tuusuario.healing

import android.app.Application
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.NotesRepository

/**
 * Clase Application personalizada para inicializar la base de datos y el repositorio
 * una sola vez durante el ciclo de vida de la aplicación.
 */
class HealingApplication : Application() {
    // 'lazy' asegura que la base de datos y el repositorio se creen solo cuando se necesiten por primera vez.
    private val database by lazy { AppDatabase.getDatabase(this) }
    val notesRepository by lazy { NotesRepository(database.noteDao()) }
}
