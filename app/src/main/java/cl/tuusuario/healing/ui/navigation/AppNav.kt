package cl.tuusuario.healing.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
// --- ¡CAMBIOS CLAVE EN LOS IMPORTS! ---
// Se reemplazan los imports de navegación estándar por los de Accompanist
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
// ---
import cl.tuusuario.healing.ui.screens.auth.LoginScreen
import cl.tuusuario.healing.ui.screens.auth.RegisterScreen
// --- ¡NUEVOS IMPORTS PARA LAS PANTALLAS DEL PROFESIONAL! ---
import cl.tuusuario.healing.ui.screens.auth.professional.PatientDetailScreen // <-- Pantalla NUEVA
import cl.tuusuario.healing.ui.screens.auth.professional.ProfPatientsScreen  // <-- Pantalla que ya tenías
import cl.tuusuario.healing.ui.screens.patient.*
import cl.tuusuario.healing.ui.screens.patient.notes.NotesScreen
import cl.tuusuario.healing.ui.screens.professional.ProfAgendaScreen
import cl.tuusuario.healing.ui.screens.professional.ProfessionalHomeScreen
import cl.tuusuario.healing.ui.screens.professional.RegisterAttentionScreen

// Tu objeto Routes no necesita cambios
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PATIENT_HOME = "patient_home"
    const val PROFESSIONAL_HOME = "professional_home"

    // Herramientas Paciente
    const val NOTES = "notes"
    const val EMERGENCY = "emergency"
    const val PERSONAL = "personal"
    const val CALENDAR = "calendar"
    const val MEDS = "meds"

    // ---- Profesional ----
    const val PROF_AGENDA = "prof_agenda"
    const val PROF_PATIENTS = "prof_patients"
    const val PROF_REGISTER = "prof_register"
    // --- ¡NUEVA RUTA AÑADIDA! ---
    const val PATIENT_DETAIL = "patient_detail"
}

// Anotación necesaria para las APIs de animación experimentales
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav(startDestination: String = Routes.LOGIN) {
    // 1. Usamos el NavController que soporta animaciones
    val nav = rememberAnimatedNavController()

    // 2. Definimos las 4 animaciones de deslizamiento que reutilizaremos
    val slideIn = slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(350))
    val slideOut = slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(350))
    val popSlideIn = slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(350))
    val popSlideOut = slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(350))


    // 3. Usamos AnimatedNavHost en lugar del NavHost estándar
    AnimatedNavHost(navController = nav, startDestination = startDestination) {

        composable(
            Routes.LOGIN,
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            LoginScreen(
                onLoginAsPatient = { nav.navigate(Routes.PATIENT_HOME) },
                onLoginAsProfessional = { nav.navigate(Routes.PROFESSIONAL_HOME) },
                onGoToRegister = { nav.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) {
            RegisterScreen(onNavigateBack = { nav.popBackStack() })
        }

        // --- NAVEGACIÓN DEL PACIENTE (SIN CAMBIOS) ---
        composable(Routes.PATIENT_HOME, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { PatientHomeScreen(nav) }
        composable(Routes.NOTES, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) {
            NotesScreen(
                onBack = { nav.popBackStack() })
        }
        composable(Routes.EMERGENCY, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { EmergencyContactScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.PERSONAL, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { PersonalDataScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.CALENDAR, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { CalendarScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.MEDS, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { MedsReminderScreen(onBack = { nav.popBackStack() }) }

        // --- NAVEGACIÓN DEL PROFESIONAL (CON CAMBIOS) ---
        composable(Routes.PROFESSIONAL_HOME, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { ProfessionalHomeScreen(nav) }
        composable(Routes.PROF_AGENDA, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { ProfAgendaScreen(onBack = { nav.popBackStack() }) }

        // --- ¡CAMBIO AQUÍ! Se completa la llamada a ProfPatientsScreen ---
        composable(
            Routes.PROF_PATIENTS,
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }
        ) {
            ProfPatientsScreen(
                // Se añade la acción de navegar al detalle del paciente
                onPatientClick = { patientId ->
                    nav.navigate("${Routes.PATIENT_DETAIL}/$patientId")
                },
                onAddPatientClick = { /* Lógica para añadir paciente, por ahora vacía */ },
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.PROF_REGISTER, enterTransition = { slideIn }, exitTransition = { slideOut }, popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }) { RegisterAttentionScreen(onBack = { nav.popBackStack() }) }


        // --- ¡NUEVA PANTALLA AÑADIDA! ---
        composable(
            route = "${Routes.PATIENT_DETAIL}/{patientId}", // La ruta acepta un argumento
            arguments = listOf(navArgument("patientId") { type = NavType.StringType }),
            enterTransition = { slideIn }, exitTransition = { slideOut },
            popEnterTransition = { popSlideIn }, popExitTransition = { popSlideOut }
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientDetailScreen(
                patientId = patientId,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
