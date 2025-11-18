package cl.tuusuario.healing.ui.screens.auth.professional

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Modelo de datos de ejemplo para un mensaje
private data class ChatMessage(
    val text: String,
    val isFromPatient: Boolean,
    val timestamp: String
)

@Composable
fun PatientChatTab(
    patientId: String // Recibe el ID para poder enviar mensajes
) {
    // --- Lógica de ejemplo ---
    val messages = remember {
        listOf(
            ChatMessage("Hola doctor, he tenido un poco de dolor de cabeza hoy.", true, "10:30 AM"),
            ChatMessage("Entendido. ¿Ha tomado su medicamento para la presión?", false, "10:31 AM"),
            ChatMessage("Sí, lo tomé hace una hora.", true, "10:32 AM"),
            ChatMessage("Perfecto. Monitoree sus síntomas y si el dolor aumenta, me avisa por aquí.", false, "10:33 AM")
        ).reversed() // Invertimos la lista para mostrar el más reciente abajo
    }

    var textState by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Lista de mensajes que ocupa el espacio disponible
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                reverseLayout = true // Muestra los items desde abajo hacia arriba
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }
            }

            // Campo de entrada de texto
            ChatInput(
                text = textState,
                onTextChange = { textState = it },
                onSendClick = {
                    // Aquí iría la lógica para enviar el mensaje
                    textState = ""
                }
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val alignment = if (message.isFromPatient) Alignment.CenterStart else Alignment.CenterEnd
    val backgroundColor = if (message.isFromPatient) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }
    val textColor = if (message.isFromPatient) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (message.isFromPatient) 0.dp else 48.dp,
                end = if (message.isFromPatient) 48.dp else 0.dp
            ),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = if (message.isFromPatient) Alignment.Start else Alignment.End
        ) {
            Text(text = message.text, color = textColor)
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(tonalElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un mensaje...") },
                maxLines = 4
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                enabled = text.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = Color.Gray
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar mensaje")
            }
        }
    }
}
