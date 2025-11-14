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
    val content: String,
    val createdAt: Long = System.currentTimeMillis() // <-- ¡NUEVO! Campo de fecha y hora
)

@Entity(tableName = "personal_data")
data class PersonalDataEntity(
    @PrimaryKey val id: Int = 1,
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

@Entity(tableName = "professionals")
data class ProfessionalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String
)

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val diagnosis: String,
    val lastActivity: String,
    val hasCriticalAlert: Boolean = false,
    val professionalOwnerId: String
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(index = true)
    val email: String,
    val passwordHash: String,
    val isAdmin: Boolean = false
)

//================================================
// 2. DAOs (Las Consultas a las Tablas)
//================================================

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY createdAt DESC") // <-- CAMBIO: Ordenamos por fecha de creación
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

    @Query("SELECT * FROM medication_reminders WHERE isTaken = 0 ORDER BY time ASC")
    fun getUntakenReminders(): Flow<List<MedsReminderEntity>>
}

@Dao
interface ProfessionalDao {
    @Upsert
    suspend fun upsertProfessional(professional: ProfessionalEntity)

    @Upsert
    suspend fun upsertPatient(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE professionalOwnerId = :professionalId")
    fun getPatientsForProfessional(professionalId: String): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE id = :patientId")
    fun getPatientById(patientId: String): Flow<PatientEntity?>
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
}


//================================================
// 3. CLASE DE LA BASE DE DATOS (El Edificio Central)
//================================================

@Database(
    entities = [
        Note::class,
        PersonalDataEntity::class,
        EmergencyContactEntity::class,
        MedsReminderEntity::class,
        ProfessionalEntity::class,
        PatientEntity::class,
        UserEntity::class
    ],
    version = 6, // <-- CAMBIO: Incremente la versión de la BD
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun personalDataDao(): PersonalDataDao
    abstract fun emergencyContactDao(): EmergencyContactDao
    abstract fun medsReminderDao(): MedsReminderDao
    abstract fun professionalDao(): ProfessionalDao
    abstract fun userDao(): UserDao

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
