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
fun PersonalDataScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val card = Color(0xFFD9D2FF)
    val pill = Color(0xFF6A5FA0)

    var name by rememberSaveable { mutableStateOf("") }
    var rut by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var blood by rememberSaveable { mutableStateOf("") }
    var allergies by rememberSaveable { mutableStateOf("") }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PatientHeader()
            Text("Datos personales", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)

            Surface(color = card, shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Domicilio") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = blood, onValueChange = { blood = it }, label = { Text("Tipo sanguíneo") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = allergies, onValueChange = { allergies = it }, label = { Text("Alergias o enfermedades") }, modifier = Modifier.fillMaxWidth())
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { /* guardar */ }, colors = ButtonDefaults.buttonColors(containerColor = pill), shape = RoundedCornerShape(50)) {
                    Text("Agregar información")
                }
                OutlinedButton(onClick = { /* editar */ }, shape = RoundedCornerShape(50)) { Text("Editar información") }
            }

            Spacer(Modifier.weight(1f))
            TextButton(onClick = onBack) { Text("Volver") }
        }
    }
}
