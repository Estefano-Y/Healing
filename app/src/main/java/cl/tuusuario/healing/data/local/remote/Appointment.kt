package cl.tuusuario.healing.data.local.remote

data class Appointment(
    val id: Long? = null,
    val patientId: Long,
    val professional: String,
    val dateTimeIso: String,
    val notes: String? = null
)