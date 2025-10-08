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
fun NotesScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)
    val card = Color(0xFFD9D2FF)
    val pill = Color(0xFF6A5FA0)

    var text by rememberSaveable { mutableStateOf("") }
    val notes = remember { mutableStateListOf<String>() }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            PatientHeader()
            Spacer(Modifier.height(12.dp))
            Text("Bienvenido a tu\nblock de notas", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(16.dp))
            Surface(color = card, shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth().height(220.dp)) {
                TextField(
                    value = text, onValueChange = { text = it },
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    placeholder = { Text("Escribe aquí...") }
                )
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { if (text.isNotBlank()) { notes += text; text = "" } },
                    colors = ButtonDefaults.buttonColors(containerColor = pill),
                    shape = RoundedCornerShape(50)
                ) { Text("Guardar texto") }

                OutlinedButton(onClick = onBack, shape = RoundedCornerShape(50)) { Text("Ver blocks guardados") }
            }

            if (notes.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Última nota:", fontWeight = FontWeight.Medium)
                Text(notes.last())
            }

            Spacer(Modifier.weight(1f))
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver") }
        }
    }
}
