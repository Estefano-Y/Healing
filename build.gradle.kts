plugins {
    // Definimos los plugins que el proyecto usará, pero sin aplicarlos todavía (apply false)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}
