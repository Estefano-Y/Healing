package cl.tuusuario.healing.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Paleta de colores vibrante y moderna
private val BluePrimary = Color(0xFF0052D4) // Un azul eléctrico y fuerte
private val BlueSecondary = Color(0xFF651FFF) // Un morado vibrante como secundario
private val BlueTertiary = Color(0xFF1DE9B6) // Un teal brillante para acentos

private val MintSecondary = Color(0xFF64FFDA) // Un menta claro para contraste y fondos

private val OnPrimary = Color.White
private val PrimaryContainer = Color(0xFFD1E4FF) // Un azul claro para los contenedores

private val DarkBluePrimary = Color(0xFF536DFE)
private val DarkBlueSecondary = Color(0xFF7C4DFF)
private val DarkBlueTertiary = Color(0xFF18FFFF)


private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Color(0xFF001A40),
    secondary = BlueSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE7F6),
    onSecondaryContainer = Color(0xFF20005D),
    tertiary = BlueTertiary,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFB2DFDB),
    onTertiaryContainer = Color(0xFF003737),
    error = Color(0xFFD32F2F),
    background = Color(0xFFF7F9FF), // Un fondo casi blanco pero azulado
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFF7F9FF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE0E2EC),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFF74777F)
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkBluePrimary,
    onPrimary = Color(0xFFE8EAF6),
    primaryContainer = Color(0xFF3F51B5),
    onPrimaryContainer = Color(0xFFC5CAE9),
    secondary = DarkBlueSecondary,
    onSecondary = Color(0xFFEDE7F6),
    secondaryContainer = Color(0xFF651FFF),
    onSecondaryContainer = Color(0xFFB388FF),
    tertiary = DarkBlueTertiary,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF00BCD4),
    onTertiaryContainer = Color(0xFF84FFFF),
    error = Color(0xFFFFB4AB),
    background = Color(0xFF121212), // Fondo oscuro estándar
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF1E1E1E), // Superficies ligeramente más claras
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC4C6CF),
    outline = Color(0xFF8E9099)
)


@Composable
fun HealingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // El color dinámico se mantiene desactivado
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
