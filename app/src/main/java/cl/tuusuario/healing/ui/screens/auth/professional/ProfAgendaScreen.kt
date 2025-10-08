package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment          // ← IMPORT CLAVE
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.tuusuario.healing.ui.screens.professional.components.ProHeader


@Composable
fun ProfAgendaScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val items = listOf(
        "09:00  •  Juan Pérez  •  Control",
        "09:30  •  Ana Díaz    •  Evaluación",
        "10:15  •  Pedro Soto  •  Seguimiento",
        "11:00  •  Camila Paz  •  Teleconsulta",
        "12:00  •  —           •  Bloque Libre",
    )

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            ProHeader(leftTitle = "Agenda")
            Text("Hoy", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { row ->
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        Text(row, modifier = Modifier.padding(16.dp))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver") }
        }
    }
}
