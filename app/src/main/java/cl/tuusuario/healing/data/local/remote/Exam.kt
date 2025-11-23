package cl.tuusuario.healing.data.local.remote

data class Exam(
    val id: Long? = null,
    val patientId: Long,
    val name: String,
    val result: String,
    val uploadedAt: Long
)