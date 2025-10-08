package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import cl.tuusuario.healing.ui.navigation.Routes
import cl.tuusuario.healing.ui.screens.professional.components.ProHeader

@Composable
fun ProfessionalHomeScreen(nav: NavHostController) {
    val bg = Color(0xFFAED9C5)
    val pill = Color(0xFF6A5FA0)

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ProHeader()
            Text("Bienvenido/a", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)

            // Acciones principales
            Button(
                onClick = { nav.navigate(Routes.PROF_AGENDA) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pill, contentColor = Color.White)
            ) { Text("Mi Agenda (hoy)") }

            OutlinedButton(
                onClick = { nav.navigate(Routes.PROF_PATIENTS) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Pacientes") }

            OutlinedButton(
                onClick = { nav.navigate(Routes.PROF_REGISTER) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Registrar Atención") }

            Spacer(Modifier.height(8.dp))
            // Extra (placeholders)
            Text("KPIs rápidos", fontWeight = FontWeight.Medium)
            Text("• Citas del día: 6\n• Asistencias: 4\n• Canceladas: 1")
        }
    }
}
