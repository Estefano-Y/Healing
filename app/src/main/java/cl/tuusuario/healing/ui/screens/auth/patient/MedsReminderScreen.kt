package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.tuusuario.healing.ui.screens.patient.components.PatientHeader

@Composable
fun MedsReminderScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val card = Color(0xFF1E1038) // morado oscuro del mock
    val pill = Color(0xFF6A5FA0)

    var medName by rememberSaveable { mutableStateOf("Paracetamol") }
    var qty by rememberSaveable { mutableStateOf("2") }
    var date by rememberSaveable { mutableStateOf("25 de Sept") }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            PatientHeader()
            Spacer(Modifier.height(8.dp))

            Surface(
                color = card,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(
                    Modifier.fillMaxSize().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier.height(8.dp))
                    Text(medName, color = Color.White, fontSize = 24.sp)

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Agendado para el día", color = Color.White)
                        Text(date, color = Color.White, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(12.dp))
                        Text("Tomar $qty", color = Color.White, fontSize = 20.sp)
                    }

                    OutlinedButton(onClick = { /* marcar tomado */ }, shape = RoundedCornerShape(50)) {
                        Text("✓ Listo", color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { /* editar */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = pill),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Editar") }

            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver") }
        }
    }
}
