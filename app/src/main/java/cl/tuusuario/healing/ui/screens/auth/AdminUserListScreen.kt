package cl.tuusuario.healing.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.viewmodels.AdminViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserListScreen(navController: NavController) {
    val context = LocalContext.current
    val repository = remember(context) {
        val db = AppDatabase.getDatabase(context)
        PatientDataRepository(
            noteDao = db.noteDao(),
            personalDataDao = db.personalDataDao(),
            emergencyContactDao = db.emergencyContactDao(),
            medsReminderDao = db.medsReminderDao(),
            professionalDao = db.professionalDao(),
            userDao = db.userDao()
        )
    }
    val viewModel: AdminViewModel = viewModel(factory = ViewModelFactory(repository))
    val users by viewModel.users.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Usuarios") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Usuarios Registrados (${users.size})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (user.isAdmin) Icons.Default.Shield else Icons.Default.Person,
                            contentDescription = if (user.isAdmin) "Admin" else "User",
                            tint = if (user.isAdmin) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Password: ${user.passwordHash}", // Consider hiding this in a real app
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { /* TODO: Implement user deletion */ }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar Usuario")
                        }
                    }
                }
            }
        }
    }
}
