package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.tuusuario.healing.ui.screens.professional.components.ProHeader

@Composable
fun ProfPatientsScreen(onBack: () -> Unit) {
    val bg = Color(0xFFAED9C5)

    // Lista fija: no requiere 'remember'
    val patients = listOf(
        "Juan Pérez – 12 atenciones",
        "Ana Díaz – 8 atenciones",
        "Pedro Soto – 5 atenciones",
        "Camila Paz – 3 atenciones"
    )

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            ProHeader(leftTitle = "Pacientes")
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(patients) { p ->
                    ElevatedCard(onClick = { /* abrir ficha */ }, modifier = Modifier.fillMaxWidth()) {
                        Text(p, modifier = Modifier.padding(16.dp))
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("Volver") }
        }
    }
}
