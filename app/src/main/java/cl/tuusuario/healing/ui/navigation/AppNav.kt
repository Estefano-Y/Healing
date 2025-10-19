package cl.tuusuario.healing.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.tuusuario.healing.ui.screens.auth.LoginScreen
import cl.tuusuario.healing.ui.screens.auth.RegisterScreen
import cl.tuusuario.healing.ui.screens.auth.professional.PatientDetailScreen
import cl.tuusuario.healing.ui.screens.auth.professional.ProfPatientsScreen
import cl.tuusuario.healing.ui.screens.patient.*
import cl.tuusuario.healing.ui.screens.patient.notes.NotesScreen
import cl.tuusuario.healing.ui.screens.professional.ProfAgendaScreen
import cl.tuusuario.healing.ui.screens.professional.ProfessionalHomeScreen
import cl.tuusuario.healing.ui.screens.professional.RegisterAttentionScreen


// El objeto Routes no necesita cambios.
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PATIENT_HOME = "patient_home"
    const val PROFESSIONAL_HOME = "professional_home"
    const val NOTES = "notes"
    const val EMERGENCY = "emergency"
    const val PERSONAL = "personal"
    const val CALENDAR = "calendar"
    const val MEDS = "meds"
    const val PROF_AGENDA = "prof_agenda"
    const val PROF_PATIENTS = "prof_patients"
    const val PROF_REGISTER = "prof_register"
    private const val PATIENT_DETAIL_ARG = "patientId"
    const val PATIENT_DETAIL = "patient_detail"
    const val PATIENT_DETAIL_ROUTE = "$PATIENT_DETAIL/{$PATIENT_DETAIL_ARG}"
    fun patientDetailRoute(patientId: String) = "$PATIENT_DETAIL/$patientId"
}


@Composable
fun AppNav(startDestination: String = Routes.LOGIN) {
    val nav = rememberNavController()

    // Las animaciones están perfectas.
    val slideIn = slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(350))
    val slideOut = slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(350))
    val popSlideIn = slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(350))
    val popSlideOut = slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(350))

    NavHost(
        navController = nav,
        startDestination = startDestination,
        enterTransition = { slideIn },
        exitTransition = { slideOut },
        popEnterTransition = { popSlideIn },
        popExitTransition = { popSlideOut }
    ) {

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

        composable(Routes.REGISTER) {
            RegisterScreen(onNavigateBack = { nav.popBackStack() })
        }

        // --- NAVEGACIÓN DEL PACIENTE ---
        composable(Routes.PATIENT_HOME) { PatientHomeScreen(nav) }
        composable(Routes.NOTES) { NotesScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.EMERGENCY) { EmergencyContactScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.PERSONAL) { PersonalDataScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.CALENDAR) { CalendarScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.MEDS) { MedsReminderScreen(onBack = { nav.popBackStack() }) }

        // --- NAVEGACIÓN DEL PROFESIONAL ---
        composable(Routes.PROFESSIONAL_HOME) { ProfessionalHomeScreen(nav) }
        composable(Routes.PROF_AGENDA) { ProfAgendaScreen(onBack = { nav.popBackStack() }) }

        // --- ¡¡¡CORRECCIÓN AQUÍ!!! ---
        composable(Routes.PROF_PATIENTS) {
            ProfPatientsScreen(
                onPatientClick = { patientId ->
                    nav.navigate(Routes.patientDetailRoute(patientId))
                },
                // El parámetro onAddPatientClick ya no se pasa,
                // porque la lógica del diálogo ahora está dentro de la pantalla.
                onBack = { nav.popBackStack() }
            )
        }
        // --- FIN DE LA CORRECCIÓN ---

        composable(Routes.PROF_REGISTER) { RegisterAttentionScreen(onBack = { nav.popBackStack() }) }

        composable(
            route = Routes.PATIENT_DETAIL_ROUTE,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            PatientDetailScreen(
                patientId = patientId,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
