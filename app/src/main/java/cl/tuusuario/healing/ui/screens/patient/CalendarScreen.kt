package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

private enum class EventType(val color: Color) {
    APPOINTMENT(Color(0xFF4C6E81)),
    TASK(Color(0xFFE57373)),
    MEAL(Color(0xFF65BBA9))
}

private data class Event(
    val id: String,
    val date: LocalDate,
    val title: String,
    val description: String,
    val type: EventType,
    val time: String? = null
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun CalendarScreen(onBack: () -> Unit) {
    val events = remember {
        listOf(
            Event("1", LocalDate.now(), "Cita", "Control con Dr. Silva", EventType.APPOINTMENT, "10:00"),
            Event("2", LocalDate.now(), "Desayuno", "Avena con frutas y nueces", EventType.MEAL, "08:00"),
            Event("3", LocalDate.now(), "Almuerzo", "Lentejas con arroz integral", EventType.MEAL, "13:00"),
            Event("4", LocalDate.now(), "Cena", "Ensalada de pollo grillado", EventType.MEAL, "20:00"),
            Event("5", LocalDate.now().plusDays(2), "Tarea", "Tomar presión arterial", EventType.TASK, "09:00"),
            Event("6", LocalDate.now().plusDays(2), "Almuerzo", "Salmón a la plancha con quínoa", EventType.MEAL, "13:30"),
            Event("7", LocalDate.now().minusDays(3), "Tarea", "Análisis de sangre en ayunas", EventType.TASK),
        ).sortedBy { it.time }
    }

    val eventsByDate = remember(events) { events.groupBy { it.date } }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    var selection by remember { mutableStateOf(LocalDate.now()) }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Agenda y Plan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    Day(
                        day = day,
                        isSelected = selection == day.date,
                        events = eventsByDate[day.date].orEmpty()
                    ) { clickedDay ->
                        selection = clickedDay.date
                    }
                },
                monthHeader = { month -> MonthHeader(month) }
            )

            Divider()

            AnimatedContent(
                targetState = selection,
                modifier = Modifier.animateContentSize(),
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "event_list_animation"
            ) { targetSelection ->
                val selectedEvents = eventsByDate[targetSelection].orEmpty()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (selectedEvents.isNotEmpty()) {
                        items(selectedEvents, key = { it.id }) { event ->
                            if (event.type == EventType.MEAL) {
                                MealPlanItem(event, modifier = Modifier.animateItemPlacement())
                            } else {
                                EventItem(event, modifier = Modifier.animateItemPlacement())
                            }
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxHeight(0.5f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No hay eventos para este día.",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Day(day: CalendarDay, isSelected: Boolean, events: List<Event>, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    day.position != DayPosition.MonthDate -> MaterialTheme.colorScheme.outline
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontSize = 14.sp
            )
            AnimatedVisibility(visible = events.isNotEmpty(), enter = scaleIn(), exit = scaleOut()) {
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    events.map { it.type.color }.distinct().take(3).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = monthFormatter.format(calendarMonth.yearMonth).replaceFirstChar { it.uppercase() },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = daysOfWeek()
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES")),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun EventItem(event: Event, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val icon = when (event.type) {
            EventType.APPOINTMENT -> Icons.Default.MedicalServices
            EventType.TASK -> Icons.Default.Checklist
            EventType.MEAL -> Icons.Default.Restaurant
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = event.type.color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(event.title, fontWeight = FontWeight.Bold)
            Text(event.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        event.time?.let {
            Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun MealPlanItem(event: Event, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(event.type.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.RestaurantMenu,
                    contentDescription = "Comida",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.Bold)
                Text(event.description, style = MaterialTheme.typography.bodyMedium)
            }
            event.time?.let {
                Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
