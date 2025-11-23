package cl.tuusuario.healing.data.local.remote

data class Patient(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val email: String? = null,
    val phone: String? = null,
    val professional: String? = null,
    val dateTimeIso: String? = null,
    val notes: String? = null
)