package cl.tuusuario.healing.ui.screens.professional

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// --- Modelos de Datos para esta pantalla ---
private enum class AppointmentType(val color: Color, val icon: ImageVector) {
    VIDEO_CALL(Color(0xFF4FC3F7), Icons.Default.Videocam),         // Azul claro para videollamada
    IN_PERSON(Color(0xFF4DB6AC), Icons.Default.MedicalServices), // Verde azulado para presencial
    PERSONAL(Color(0xFFBDBDBD), Icons.Default.SelfImprovement)  // Gris para tarea personal
}

private data class ProfessionalEvent(
    val id: String,
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
    val title: String, // Ej: "Consulta con Juan Pérez"
    val subtitle: String?, // Ej: "Revisión de resultados"
    val type: AppointmentType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfAgendaScreen(onBack: () -> Unit) {

    // --- Datos de Ejemplo ---
    val events = remember {
        listOf(
            ProfessionalEvent("1", LocalDate.now(), "09:00", "09:45", "Control con Ana López", "Seguimiento semanal", AppointmentType.VIDEO_CALL),
            ProfessionalEvent("2", LocalDate.now(), "10:00", "10:45", "Consulta con Carlos Soto", "Primera visita", AppointmentType.IN_PERSON),
            ProfessionalEvent("3", LocalDate.now(), "11:00", "11:30", "Revisar análisis", "Laboratorio Dr. Simi", AppointmentType.PERSONAL),
            ProfessionalEvent("4", LocalDate.now(), "12:00", "12:45", "Control con Luisa Fernández", null, AppointmentType.VIDEO_CALL),
            ProfessionalEvent("5", LocalDate.now().plusDays(1), "15:00", "15:45", "Consulta con Pedro Morales", null, AppointmentType.IN_PERSON),
        ).sortedBy { it.startTime }
    }

    val eventsByDate = remember(events) { events.groupBy { it.date } }
    var selection by remember { mutableStateOf(LocalDate.now()) }

    // --- Configuración del Calendario Horizontal ---
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Agenda") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Lógica para añadir nuevo evento */ }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Evento")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- Calendario Horizontal para seleccionar el día ---
            HorizontalCalendar(
                modifier = Modifier.padding(bottom = 8.dp),
                state = calendarState,
                dayContent = { day ->
                    Day(day, isSelected = selection == day.date, hasEvents = eventsByDate.containsKey(day.date)) {
                        selection = it.date
                    }
                },
                monthHeader = { MonthHeader(it) }
            )

            Divider()

            // --- Lista de Eventos para el día seleccionado ---
            val selectedEvents = eventsByDate[selection].orEmpty()
            if (selectedEvents.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(selectedEvents, key = { it.id }) { event ->
                        EventTimeLineItem(event)
                    }
                }
            } else {
                // Mensaje para cuando no hay eventos
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay eventos programados para este día.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}


// --- Componentes del Calendario (más compactos) ---

@Composable
private fun Day(day: CalendarDay, isSelected: Boolean, hasEvents: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES")).take(1).uppercase(),
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = day.date.dayOfMonth.toString(),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            if (hasEvents) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary))
            } else {
                Spacer(modifier = Modifier.size(6.dp))
            }
        }
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES")) }
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = monthFormatter.format(calendarMonth.yearMonth).replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


// --- Componente para la lista de eventos en formato "timeline" ---

@Composable
private fun EventTimeLineItem(event: ProfessionalEvent) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Columna de la hora
        Column(horizontalAlignment = Alignment.End) {
            Text(event.startTime, fontWeight = FontWeight.Bold)
            Text(event.endTime, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }

        Spacer(Modifier.width(16.dp))

        // Columna de la línea de tiempo y la tarjeta
        Row {
            // Línea vertical
            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(MaterialTheme.colorScheme.outlineVariant))

            Spacer(Modifier.width(16.dp))

            // Tarjeta del evento
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp),
                colors = CardDefaults.cardColors(containerColor = event.type.color.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = event.type.icon,
                        contentDescription = null,
                        tint = event.type.color,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(event.title, fontWeight = FontWeight.SemiBold)
                        event.subtitle?.let {
                            Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
