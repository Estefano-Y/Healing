package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PersonalDataViewModel : ViewModel() {

    // Estado para cada campo del formulario
    var name by mutableStateOf("")
        private set
    var lastName by mutableStateOf("")
        private set
    var rut by mutableStateOf("")
        private set
    var birthDate by mutableStateOf("")
        private set
    var address by mutableStateOf("")
        private set
    var phone by mutableStateOf("")
        private set

    // Funciones para actualizar el estado desde la UI
    fun onNameChange(newName: String) {
        name = newName
    }

    fun onLastNameChange(newLastName: String) {
        lastName = newLastName
    }

    fun onRutChange(newRut: String) {
        rut = newRut
    }

    fun onBirthDateChange(newBirthDate: String) {
        birthDate = newBirthDate
    }

    fun onAddressChange(newAddress: String) {
        address = newAddress
    }

    fun onPhoneChange(newPhone: String) {
        phone = newPhone
    }

    // Lógica para guardar los datos (aquí puedes añadir la conexión a tu base de datos o API)
    fun onSaveData() {
        // Por ahora, solo imprimimos los datos en la consola para verificar
        println("Guardando datos...")
        println("Nombre: $name")
        println("Apellido: $lastName")
        println("RUT: $rut")
        println("Fecha de Nacimiento: $birthDate")
        println("Dirección: $address")
        println("Teléfono: $phone")
        // Aquí iría la lógica para persistir los datos
    }
}
