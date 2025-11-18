package cl.tuusuario.healing.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.tuusuario.healing.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Admin Console") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido, Administrador")
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { navController.navigate(Routes.ADMIN_USER_LIST) }) {
                Text("Gestionar Usuarios")
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(Routes.PROF_AGENDA) }) {
                Text("Gestionar Agendas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(Routes.MEDS) }) {
                Text("Gestionar Medicamentos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(Routes.EMERGENCY) }) {
                Text("Gestionar Contacto de Emergencia")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(Routes.PERSONAL) }) {
                Text("Gestionar Datos Personales")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(Routes.MAP) }) {
                Text("Mapa")
            }
        }
    }
}