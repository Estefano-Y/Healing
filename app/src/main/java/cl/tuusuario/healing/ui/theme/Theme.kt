package cl.tuusuario.healing.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Paleta de colores principal - Inspirada en tonos de salud y calma
private val GreenPrimary = Color(0xFF006C4C)
private val GreenSecondary = Color(0xFF4C6358)
private val GreenTertiary = Color(0xFF3F6375)

private val GreenOnPrimary = Color.White
private val GreenPrimaryContainer = Color(0xFF8CF8C7)

private val DarkGreenPrimary = Color(0xFF70DBAC)
private val DarkGreenSecondary = Color(0xFFB4CCBD)
private val DarkGreenTertiary = Color(0xFFA6C9DD)


private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = Color(0xFF002114),
    secondary = GreenSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCEE9DA),
    onSecondaryContainer = Color(0xFF092017),
    tertiary = GreenTertiary,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC3E9FD),
    onTertiaryContainer = Color(0xFF001F2A),
    error = Color(0xFFBA1A1A),
    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1A),
    surfaceVariant = Color(0xFFDDE5DE),
    onSurfaceVariant = Color(0xFF414944),
    outline = Color(0xFF717974)
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreenPrimary,
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF005138),
    onPrimaryContainer = GreenPrimaryContainer,
    secondary = DarkGreenSecondary,
    onSecondary = Color(0xFF1E352B),
    secondaryContainer = Color(0xFF354B40),
    onSecondaryContainer = Color(0xFFCEE9DA),
    tertiary = DarkGreenTertiary,
    onTertiary = Color(0xFF0B3545),
    tertiaryContainer = Color(0xFF264C5D),
    onTertiaryContainer = Color(0xFFC3E9FD),
    error = Color(0xFFFFB4AB),
    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF414944),
    onSurfaceVariant = Color(0xFFC1C9C3),
    outline = Color(0xFF8B938D)
)


@Composable
fun HealingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivamos el color dinámico para mantener nuestra marca
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
