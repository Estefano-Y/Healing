package cl.tuusuario.healing.data.local.repository

import cl.tuusuario.healing.data.local.Note
import cl.tuusuario.healing.data.local.NoteDao
import cl.tuusuario.healing.data.local.EmergencyContactDao
import cl.tuusuario.healing.data.local.EmergencyContactEntity
import cl.tuusuario.healing.data.local.MedsReminderDao
import cl.tuusuario.healing.data.local.MedsReminderEntity
import cl.tuusuario.healing.data.local.PersonalDataDao
import cl.tuusuario.healing.data.local.PersonalDataEntity
import cl.tuusuario.healing.data.local.PatientEntity
import cl.tuusuario.healing.data.local.ProfessionalDao
import cl.tuusuario.healing.data.local.ProfessionalEntity
import cl.tuusuario.healing.data.local.UserDao
import cl.tuusuario.healing.data.local.UserEntity
import cl.tuusuario.healing.data.local.remote.openfda.DrugLabelItem
import cl.tuusuario.healing.data.local.remote.openfda.OpenFdaClient
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que actúa como intermediario para todos los datos.
 * Recibe los DAOs como parámetros para poder acceder a la base de datos.
 */
class PatientDataRepository(
    private val noteDao: NoteDao,
    private val personalDataDao: PersonalDataDao,
    private val emergencyContactDao: EmergencyContactDao,
    private val medsReminderDao: MedsReminderDao,
    private val professionalDao: ProfessionalDao,
    private val userDao: UserDao
) {

    // --- Funciones para Notas ---
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    // --- ¡CORRECCIÓN! Se añade la palabra clave suspend ---
    suspend fun upsertNote(note: Note) = noteDao.upsertNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    // --- Funciones para Datos Personales ---
    fun getPersonalData(): Flow<PersonalDataEntity?> = personalDataDao.getPersonalData()
    suspend fun upsertPersonalData(data: PersonalDataEntity) = personalDataDao.upsertPersonalData(data)

    // --- Funciones para Contacto de Emergencia ---
    fun getEmergencyContact(): Flow<EmergencyContactEntity?> = emergencyContactDao.getEmergencyContact()
    suspend fun upsertEmergencyContact(contact: EmergencyContactEntity) = emergencyContactDao.upsertEmergencyContact(contact)

    // --- Funciones para Recordatorios de Medicamentos ---
    fun getAllMedsReminders(): Flow<List<MedsReminderEntity>> = medsReminderDao.getAllReminders()
    suspend fun upsertMedsReminder(reminder: MedsReminderEntity) = medsReminderDao.upsertReminder(reminder)
    suspend fun deleteMedsReminder(reminderId: Int) = medsReminderDao.deleteReminderById(reminderId)
    fun getUntakenReminders(): Flow<List<MedsReminderEntity>> = medsReminderDao.getUntakenReminders()

    // --- Funciones para Profesionales ---
    fun getPatientsForProfessional(professionalId: String): Flow<List<PatientEntity>> {
        return professionalDao.getPatientsForProfessional(professionalId)
    }

    fun getPatientById(patientId: String): Flow<PatientEntity?> {
        return professionalDao.getPatientById(patientId)
    }

    suspend fun upsertPatient(patient: PatientEntity) {
        professionalDao.upsertPatient(patient)
    }

    // --- Funciones para Registro y Login de Usuarios ---
    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        val passwordHash = password
        val user = UserEntity(name = name, email = email, passwordHash = passwordHash)
        return userDao.insertUser(user) != -1L
    }

    suspend fun findUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    /**
     * Busca información de un medicamento en openFDA por su nombre genérico.
     * Ej: "ibuprofen", "paracetamol"
     */
    suspend fun getDrugInfoFromFda(genericName: String): DrugLabelItem? {
        // Armamos el string de búsqueda según la sintaxis de openFDA, usando comillas
        val query = "openfda.generic_name:\"${genericName.lowercase()}\""
        val response = OpenFdaClient.api.searchDrugLabel(search = query, limit = 1)

        // Devolvemos el primer resultado si existe
        return response.results?.firstOrNull()
    }

    // --- Función para añadir datos de prueba fácilmente ---
    suspend fun addDummyProfessionalData() {
        val professionalId = "dr.house@example.com"
        professionalDao.upsertProfessional(
            ProfessionalEntity(id = professionalId, name = "Dr. Gregory House", specialty = "Nefrología")
        )
        upsertPatient(
            PatientEntity("p001", "Juan Pérez", 45, "Hipertensión", "Hoy, 14:30", true, professionalId)
        )
        upsertPatient(
            PatientEntity("p002", "Ana Martínez", 32, "Diabetes Tipo 2", "Ayer, 10:15", false, professionalId)
        )
        upsertPatient(
            PatientEntity("p003", "Carlos Soto", 58, "Post-operatorio", "Hace 3 días", false, professionalId)
        )
    }
}
