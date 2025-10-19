package cl.tuusuario.healing.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

//============================================
// 1. ENTIDADES (Las Tablas de la Base de Datos)
//============================================

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String
)

@Entity(tableName = "personal_data")
data class PersonalDataEntity(
    @PrimaryKey val id: Int = 1, // Usamos un ID fijo para que siempre sea el mismo registro
    val name: String,
    val birthDate: String,
    val bloodType: String,
    val allergies: String
)

@Entity(tableName = "emergency_contact")
data class EmergencyContactEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val relationship: String,
    val phone: String
)

@Entity(tableName = "medication_reminders")
data class MedsReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val medName: String,
    val dose: String,
    val time: String,
    val isTaken: Boolean = false
)

//================================================
// 2. DAOs (Las Consultas a las Tablas)
//================================================

@Dao
interface NoteDao {
    @Upsert
    fun upsertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<Note>>
}

@Dao
interface PersonalDataDao {
    @Upsert
    suspend fun upsertPersonalData(data: PersonalDataEntity)

    @Query("SELECT * FROM personal_data WHERE id = 1")
    fun getPersonalData(): Flow<PersonalDataEntity?>
}

@Dao
interface EmergencyContactDao {
    @Upsert
    suspend fun upsertEmergencyContact(contact: EmergencyContactEntity)

    @Query("SELECT * FROM emergency_contact WHERE id = 1")
    fun getEmergencyContact(): Flow<EmergencyContactEntity?>
}

@Dao
interface MedsReminderDao {
    @Upsert
    suspend fun upsertReminder(reminder: MedsReminderEntity)

    @Query("DELETE FROM medication_reminders WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: Int)

    @Query("SELECT * FROM medication_reminders ORDER BY time ASC")
    fun getAllReminders(): Flow<List<MedsReminderEntity>>
}

//================================================
// 3. CLASE DE LA BASE DE DATOS (El Edificio Central)
//================================================

@Database(
    entities = [
        Note::class,
        PersonalDataEntity::class,
        EmergencyContactEntity::class,
        MedsReminderEntity::class
    ],
    version = 2, // Incrementa este número si cambias la estructura de las tablas
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun personalDataDao(): PersonalDataDao
    abstract fun emergencyContactDao(): EmergencyContactDao
    abstract fun medsReminderDao(): MedsReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "healing_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
