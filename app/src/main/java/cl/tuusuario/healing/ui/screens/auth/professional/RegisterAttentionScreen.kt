package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.tuusuario.healing.ui.screens.professional.components.ProHeader


@Composable
fun RegisterAttentionScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val pill = Color(0xFF6A5FA0)

    var patient by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var summary by rememberSaveable { mutableStateOf("") }
    var next by rememberSaveable { mutableStateOf("") }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ProHeader(leftTitle = "Registrar Atención")
            OutlinedTextField(value = patient, onValueChange = { patient = it }, label = { Text("Paciente") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Fecha/Hora") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = summary, onValueChange = { summary = it },
                label = { Text("Resumen/Diagnóstico") }, modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            OutlinedTextField(value = next, onValueChange = { next = it }, label = { Text("Próximos pasos") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = { /* guardar */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pill, contentColor = Color.White)
            ) { Text("Guardar atención") }

            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver") }
        }
    }
}
