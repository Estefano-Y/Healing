package cl.tuusuario.healing.data.local.repository

// --- ¡Nuevos imports! ---
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
    private val professionalDao: ProfessionalDao
) {

    // --- Funciones para Notas ---
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    fun upsertNote(note: Note) = noteDao.upsertNote(note)
    fun deleteNote(note: Note) = noteDao.deleteNote(note)

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


    // --- NUEVAS FUNCIONES PARA EL PROFESIONAL ---

    fun getPatientsForProfessional(professionalId: String): Flow<List<PatientEntity>> {
        return professionalDao.getPatientsForProfessional(professionalId)
    }

    fun getPatientById(patientId: String): Flow<PatientEntity?> {
        return professionalDao.getPatientById(patientId)
    }

    // --- ¡¡¡CORRECCIÓN AQUÍ!!! ---
    // Se añade la función que faltaba para que el ViewModel pueda guardar pacientes.
    suspend fun upsertPatient(patient: PatientEntity) {
        professionalDao.upsertPatient(patient)
    }
    // --- FIN DE LA CORRECCIÓN ---


    // --- Función para añadir datos de prueba fácilmente ---
    // La usaremos para no tener que registrar todo a mano al probar.
    suspend fun addDummyProfessionalData() {
        val professionalId = "dr.house@example.com"
        professionalDao.upsertProfessional(
            ProfessionalEntity(id = professionalId, name = "Dr. Gregory House", specialty = "Nefrología")
        )
        upsertPatient( // Ahora podemos llamar a nuestra propia función de repositorio
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
