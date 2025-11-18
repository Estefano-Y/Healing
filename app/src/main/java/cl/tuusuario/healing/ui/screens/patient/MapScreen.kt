package cl.tuusuario.healing.ui.screens.patient

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit
) {
    // Coordenadas para "Antonio Varas 666, 7500961 Providencia, Región Metropolitana"
    val addressLocation = LatLng(-33.4333, -70.6167)

    // Estado de la cámara (zoom y posición inicial)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(addressLocation, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de centros de salud") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            // Marcador para la dirección
            Marker(
                state = MarkerState(position = addressLocation),
                title = "Antonio Varas 666",
                snippet = "Providencia, Región Metropolitana"
            )

            // Aquí podrías agregar más marcadores si quieres
        }
    }
}