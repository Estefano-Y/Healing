package cl.tuusuario.healing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.tuusuario.healing.ui.screens.auth.LoginScreen
import cl.tuusuario.healing.ui.screens.auth.RegisterScreen // Asegúrate que la importación esté
import cl.tuusuario.healing.ui.screens.patient.*
import cl.tuusuario.healing.ui.screens.professional.ProfessionalHomeScreen
import cl.tuusuario.healing.ui.screens.professional.ProfAgendaScreen
import cl.tuusuario.healing.ui.screens.professional.ProfPatientsScreen
import cl.tuusuario.healing.ui.screens.professional.RegisterAttentionScreen

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
}

@Composable
fun AppNav(startDestination: String = Routes.LOGIN) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginAsPatient = { nav.navigate(Routes.PATIENT_HOME) },
                onLoginAsProfessional = { nav.navigate(Routes.PROFESSIONAL_HOME) },
                onGoToRegister = { nav.navigate(Routes.REGISTER) }
            )
        }

        // --- LÍNEA CORREGIDA ---
        composable(Routes.REGISTER) {
            RegisterScreen(onNavigateBack = { nav.popBackStack() })
        }

        // Paciente
        composable(Routes.PATIENT_HOME) { PatientHomeScreen(nav) }
        composable(Routes.NOTES) { NotesScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.EMERGENCY) { EmergencyContactScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.PERSONAL) { PersonalDataScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.CALENDAR) { CalendarScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.MEDS) { MedsReminderScreen(onBack = { nav.popBackStack() }) }

        // Profesional
        composable(Routes.PROFESSIONAL_HOME) { ProfessionalHomeScreen(nav) }
        composable(Routes.PROF_AGENDA) { ProfAgendaScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.PROF_PATIENTS) { ProfPatientsScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.PROF_REGISTER) { RegisterAttentionScreen(onBack = { nav.popBackStack() }) }
    }
}
