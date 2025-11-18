package cl.tuusuario.healing

import android.app.Application

/**
 * Clase Application personalizada.
 * En el futuro, la usaremos para inicializar Hilt (inyección de dependencias).
 * Por ahora, puede estar casi vacía.
 */
class HealingApplication : Application() {
    // Ya no necesitamos crear repositorios aquí,
    // porque lo estamos haciendo a nivel de cada pantalla (Composable).
    // Dejamos esta clase preparada para cuando integremos Hilt.
    override fun onCreate() {
        super.onCreate()
        // Aquí se pueden inicializar librerías globales si fuera necesario.
    }
}
