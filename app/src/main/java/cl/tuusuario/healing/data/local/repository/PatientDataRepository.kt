package cl.tuusuario.healing.data.local.repository

// --- ¡CORRECCIÓN AQUÍ! AÑADIMOS LOS IMPORTS QUE FALTAN ---
import cl.tuusuario.healing.data.local.Note
import cl.tuusuario.healing.data.local.NoteDao
// --- FIN DE LA CORRECCIÓN ---

import cl.tuusuario.healing.data.local.EmergencyContactDao
import cl.tuusuario.healing.data.local.EmergencyContactEntity
import cl.tuusuario.healing.data.local.MedsReminderDao
import cl.tuusuario.healing.data.local.MedsReminderEntity
import cl.tuusuario.healing.data.local.PersonalDataDao
import cl.tuusuario.healing.data.local.PersonalDataEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que actúa como intermediario para todos los datos
 * relacionados con el paciente.
 * Recibe los DAOs como parámetros para poder acceder a la base de datos.
 */
class PatientDataRepository(
    private val noteDao: NoteDao,
    private val personalDataDao: PersonalDataDao,
    private val emergencyContactDao: EmergencyContactDao,
    private val medsReminderDao: MedsReminderDao
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
}
