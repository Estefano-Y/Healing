package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.tuusuario.healing.ui.screens.patient.components.PatientHeader

@Composable
fun CalendarScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val dayBg = Color(0xFF8E8E8E)

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            PatientHeader()
            Spacer(Modifier.height(8.dp))
            Text("Calendario", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text("Septiembre", fontSize = 22.sp)

            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("L","M","M","J","V","S","D").forEach { Text(it, fontSize = 16.sp) }
            }
            Spacer(Modifier.height(8.dp))

            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                var day = 1
                repeat(5) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        repeat(7) {
                            if (day <= 30) {
                                Box(
                                    modifier = Modifier.size(40.dp).background(dayBg, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) { Text("$day", color = Color.White) }
                                day++
                            } else {
                                Box(Modifier.size(40.dp))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth(), shape = CircleShape) { Text("Salir") }
        }
    }
}
