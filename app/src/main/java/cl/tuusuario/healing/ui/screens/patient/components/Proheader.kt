package cl.tuusuario.healing.ui.screens.professional.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProHeader(
    leftTitle: String = "Panel Profesional",
    rightTime: String = "14:20 p. m.",
    rightDate: String = "Jueves 25 de sept"
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(leftTitle, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        Column(horizontalAlignment = Alignment.End) {
            Text(rightTime, fontSize = 11.sp)
            Text(rightDate, fontSize = 11.sp)
        }
    }
    Spacer(Modifier.height(8.dp))
}
