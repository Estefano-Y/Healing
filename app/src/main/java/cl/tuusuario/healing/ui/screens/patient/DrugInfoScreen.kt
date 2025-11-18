package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.tuusuario.healing.ui.screens.viewmodels.DrugInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrugInfoScreen(
    viewModel: DrugInfoViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información de medicamentos (FDA)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Nombre genérico (ej: ibuprofeno)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { viewModel.searchDrug(query) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Buscar")
            }
            Spacer(Modifier.height(24.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.drugItem != null -> {
                    val item = uiState.drugItem!!
                    val genericName = item.openfda?.generic_name?.joinToString() ?: "Desconocido"
                    val brandName = item.openfda?.brand_name?.joinToString() ?: "—"
                    val purpose = item.purpose?.joinToString("\n") ?: "No disponible"
                    val warnings = item.warnings?.joinToString("\n\n") ?: "No disponible"

                    Text("Nombre genérico:", fontWeight = FontWeight.Bold)
                    Text(genericName)
                    Spacer(Modifier.height(8.dp))

                    Text("Nombre de marca:", fontWeight = FontWeight.Bold)
                    Text(brandName)
                    Spacer(Modifier.height(8.dp))

                    Text("Propósito:", fontWeight = FontWeight.Bold)
                    Text(purpose)
                    Spacer(Modifier.height(8.dp))

                    Text("Advertencias:", fontWeight = FontWeight.Bold)
                    Text(warnings)
                    Spacer(Modifier.height(16.dp))

                    Text(
                        "⚠️ Esta información es referencial y no reemplaza la evaluación de un profesional de la salud.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}