// El 'package' es correcto gracias al movimiento de la carpeta
package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <-- Import correcto para LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.tuusuario.healing.HealingApplication
// --- IMPORTS DEFINITIVOS ---
import cl.tuusuario.healing.data.local.Note // <-- Importa la clase de datos 'Note'
import cl.tuusuario.healing.ui.screens.patient.notes.NotesViewModel
import cl.tuusuario.healing.ui.screens.patient.notes.NotesViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repository = (context.applicationContext as HealingApplication).notesRepository
    val viewModel: NotesViewModel = viewModel(factory = NotesViewModelFactory(repository))

    val uiState by viewModel.uiState.collectAsState()
    var showAddNoteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Notas Persistentes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddNoteDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Nota")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (uiState.notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Añade tu primera nota...")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // --- SINTAXIS DE ITEMS CORREGIDA ---
                    items(
                        items = uiState.notes,
                        key = { note -> note.id } // La key sigue siendo el id
                    ) { note ->
                        NoteItem(
                            note = note,
                            onDelete = {
                                viewModel.deleteNote(note)
                                coroutineScope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Nota eliminada",
                                        actionLabel = "Deshacer",
                                        duration = SnackbarDuration.Long
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoDelete()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { showAddNoteDialog = false },
            onConfirm = { title, content ->
                viewModel.addNote(title, content)
                showAddNoteDialog = false
            }
        )
    }
}

// El parámetro ahora es de tipo 'Note', que es la clase correcta
@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Nota", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// El diálogo no necesita cambios
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNoteDialog(onDismiss: () -> Unit, onConfirm: (title: String, content: String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Nota") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Contenido") })
            }
        },
        confirmButton = { Button(onClick = { onConfirm(title, content) }) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

