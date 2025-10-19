package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.navigation.NavHostController
import cl.tuusuario.healing.ui.navigation.Routes

@Composable
fun PatientHomeScreen(nav: NavHostController) {
    val bg = Color(0xFFAED9C5)
    val pill = Color(0xFF6A5FA0)
    val pillLight = Color(0xFF9A92C9)
    var progress by remember { mutableStateOf(0.75f) }

    Surface(color = bg, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mi Agenda", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
                Column(horizontalAlignment = Alignment.End) {
                    Text("14:20 p. m.", fontSize = 12.sp)
                    Text("Jueves 25 de sept", fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Progreso del día", fontSize = 14.sp)
                Text("${(progress * 100).toInt()}%", fontSize = 14.sp)
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .background(pillLight, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(horizontal = 10.dp),
                    color = pill,
                    trackColor = Color.Transparent
                )
            }

            Spacer(Modifier.height(22.dp))
            Text("Tracker de hábitos", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(12.dp))

            val habits = listOf(
                HabitUi("Agua", Icons.Filled.LocalDrink),
                HabitUi("Trotar", Icons.Filled.DirectionsRun),
                HabitUi("Leer", Icons.Filled.MenuBook),
                HabitUi("Comer Sano", Icons.Filled.Restaurant),
                HabitUi("Ejercicios", Icons.Filled.FitnessCenter),
                HabitUi("Pasear\nmascota", Icons.Filled.Pets),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                userScrollEnabled = false
            ) {
                items(habits) { h ->
                    HabitItem(h, pill, pillLight) {
                        progress = (progress + 0.05f).coerceAtMost(1f)
                    }
                }
            }

            Spacer(Modifier.height(22.dp))
            Text("Herramientas", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(10.dp))

            val tools = listOf(
                "Notas" to Routes.NOTES,
                "Horario\nmedicamentos" to Routes.MEDS,
                "Contacto de\nemergencia" to Routes.EMERGENCY,
                "Datos\npersonales" to Routes.PERSONAL,
                "Calendario" to Routes.CALENDAR,
                "Plan de\nalimentación" to Routes.CALENDAR // placeholder
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(tools) { (label, route) ->
                    PillButton(text = label, color = pill) { nav.navigate(route) }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

private data class HabitUi(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
private fun HabitItem(
    habit: HabitUi,
    pill: Color,
    pillLight: Color,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(color = pillLight, shape = RoundedCornerShape(100), modifier = Modifier.size(74.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                IconButton(onClick = onClick) { Icon(habit.icon, contentDescription = habit.label, tint = Color.White) }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(habit.label, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 14.sp)
    }
}

@Composable
private fun PillButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        modifier = Modifier.fillMaxWidth().height(42.dp),
        contentPadding = PaddingValues(horizontal = 14.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text, textAlign = TextAlign.Center, fontSize = 13.sp, lineHeight = 15.sp)
    }
}
