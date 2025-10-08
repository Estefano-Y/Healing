package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.tuusuario.healing.ui.screens.patient.components.PatientHeader

@Composable
fun EmergencyContactScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val card = Color(0xFFD9D2FF)
    val pill = Color(0xFF6A5FA0)

    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PatientHeader()
            Text("Contacto de\nemergencia", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)

            Surface(color = card, shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Número de celular") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dónde vive") }, modifier = Modifier.fillMaxWidth())
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { /* guardar */ }, colors = ButtonDefaults.buttonColors(containerColor = pill), shape = RoundedCornerShape(50)) {
                    Text("Agregar contacto")
                }
                OutlinedButton(onClick = { /* editar */ }, shape = RoundedCornerShape(50)) { Text("Editar contacto") }
            }

            Spacer(Modifier.weight(1f))
            TextButton(onClick = onBack) { Text("Volver") }
        }
    }
}
