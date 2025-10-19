package cl.tuusuario.healing.ui.screens.patient.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
// --- ¡IMPORTS FINALES Y CORRECTOS! ---
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.Note
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
// La ruta correcta NO incluye 'screens'
import cl.tuusuario.healing.ui.screens.viewmodels.NotesViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = remember {
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao()
        )
    }
    // Con la ViewModelFactory y el ViewModel correctos, esto funciona.
    val viewModel: NotesViewModel = viewModel(factory = ViewModelFactory(repository))

    // La línea del error ahora encontrará `notesState` en el `viewModel` correctamente importado.
    val notesState by viewModel.notesState.collectAsState()

    // ... (El resto del archivo es idéntico y correcto)
    var showAddNoteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Notas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddNoteDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Nota")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (notesState.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Añade tu primera nota...")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = notesState,
                        key = { note -> note.id }
                    ) { note ->
                        NoteItem(
                            note = note,
                            onDelete = {
                                viewModel.deleteNote(note)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = "Nota eliminada")
                                }
                            },
                            modifier = Modifier.animateItemPlacement(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
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
                val newNote = Note(title = title, content = content)
                viewModel.upsertNote(newNote)
                showAddNoteDialog = false
            }
        )
    }
}

// ... (NoteItem y AddNoteDialog se quedan igual)
@Composable
fun NoteItem(note: Note, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
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
