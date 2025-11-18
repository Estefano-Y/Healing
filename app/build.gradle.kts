plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "cl.tuusuario.healing"
    compileSdk = 34

    defaultConfig {
        applicationId = "cl.tuusuario.healing"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // 1. HABILITA EL CORE LIBRARY DESUGARING
        isCoreLibraryDesugaringEnabled = true
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Usamos el Bill of Materials (BOM) de Compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Dependencias principales de AndroidX y UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0-beta01")

    // Animaciones para la Navegación
    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")

    // Dependencias de Compose UI
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Librería para un calendario de Compose altamente personalizable
    implementation("com.kizitonwose.calendar:compose:2.5.1")

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Room (Base de Datos)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit para llamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // Conversor JSON → objetos Kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // (Opcional) logs de red para depurar
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 2. AÑADE LA DEPENDENCIA PARA EL DESUGARING
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Google Maps para Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Maps Compose (mapa directamente en Jetpack Compose)
    implementation("com.google.maps.android:maps-compose:4.4.1")

    // Dependencias de Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}