package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Modelo de datos simple para un contacto
data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val relationship: String // Ej: "Padre", "Amigo", etc.
)

class EmergencyContactViewModel : ViewModel() {

    // Lista observable de contactos.
    // Usaremos datos de ejemplo por ahora.
    private val _contacts = mutableStateOf(
        listOf(
            Contact(1, "Ana Martinez", "+56987654321", "Madre"),
            Contact(2, "Carlos Soto", "+56912345678", "Hermano"),
            Contact(3, "Dr. Eduardo Rios", "+56225550102", "Médico Tratante")
        )
    )
    val contacts: State<List<Contact>> = _contacts

    // Variables para el diálogo de "Agregar Contacto"
    var showAddContactDialog by mutableStateOf(false)
        private set
    var newContactName by mutableStateOf("")
    var newContactPhone by mutableStateOf("")
    var newContactRelationship by mutableStateOf("")

    // Funciones para manejar el diálogo
    fun onShowAddContactDialog() {
        showAddContactDialog = true
    }
    fun onDismissAddContactDialog() {
        showAddContactDialog = false
        // Limpiar campos al cerrar
        newContactName = ""
        newContactPhone = ""
        newContactRelationship = ""
    }

    // Funciones para actualizar los campos del nuevo contacto
    fun onNameChange(name: String) { newContactName = name }
    fun onPhoneChange(phone: String) { newContactPhone = phone }
    fun onRelationshipChange(relationship: String) { newContactRelationship = relationship }

    fun addContact() {
        if (newContactName.isNotBlank() && newContactPhone.isNotBlank()) {
            val newContact = Contact(
                id = (_contacts.value.maxOfOrNull { it.id } ?: 0) + 1,
                name = newContactName,
                phone = newContactPhone,
                relationship = newContactRelationship
            )
            // Añadir el nuevo contacto a la lista
            _contacts.value = _contacts.value + newContact
            onDismissAddContactDialog() // Cierra y limpia el diálogo
        }
    }

    fun removeContact(contact: Contact) {
        _contacts.value = _contacts.value.filter { it.id != contact.id }
    }
}
