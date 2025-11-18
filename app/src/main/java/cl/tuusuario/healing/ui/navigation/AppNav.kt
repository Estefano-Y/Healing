package cl.tuusuario.healing.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.screens.auth.AdminHomeScreen
import cl.tuusuario.healing.ui.screens.auth.AdminUserListScreen
import cl.tuusuario.healing.ui.screens.auth.LoginScreen
import cl.tuusuario.healing.ui.screens.auth.RegisterScreen
import cl.tuusuario.healing.ui.screens.auth.professional.PatientDetailScreen
import cl.tuusuario.healing.ui.screens.auth.professional.ProfPatientsScreen
import cl.tuusuario.healing.ui.screens.auth.professional.ProfessionalHomeScreen
import cl.tuusuario.healing.ui.screens.patient.*
import cl.tuusuario.healing.ui.screens.patient.notes.NotesScreen
import cl.tuusuario.healing.ui.screens.professional.ProfAgendaScreen
import cl.tuusuario.healing.ui.screens.professional.RegisterAttentionScreen
import cl.tuusuario.healing.ui.screens.viewmodels.DrugInfoViewModel
import cl.tuusuario.healing.ui.screens.viewmodels.ViewModelFactory


object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFESSIONAL_HOME = "professional_home"
    const val NOTES = "notes"
    const val EMERGENCY = "emergency"
    const val PERSONAL = "personal"
    const val CALENDAR = "calendar"
    const val MEDS = "meds"
    const val PROF_AGENDA = "prof_agenda"
    const val PROF_PATIENTS = "prof_patients"
    const val PROF_REGISTER = "prof_register"
    const val ADMIN_HOME = "admin_home"
    const val ADMIN_USER_LIST = "admin_user_list"
    const val DRUG_INFO = "drug_info"
    const val MAP = "map"

    // Rutas dinámicas para detalles de paciente y home de paciente
    private const val PATIENT_DETAIL_ARG = "patientId"
    private const val PATIENT_HOME_ARG = "userName"

    const val PATIENT_DETAIL = "patient_detail"
    const val PATIENT_HOME = "patient_home"

    const val PATIENT_DETAIL_ROUTE = "$PATIENT_DETAIL/{$PATIENT_DETAIL_ARG}"
    const val PATIENT_HOME_ROUTE = "$PATIENT_HOME/{$PATIENT_HOME_ARG}"

    fun patientDetailRoute(patientId: String) = "$PATIENT_DETAIL/$patientId"
    fun patientHomeRoute(userName: String) = "$PATIENT_HOME/$userName"
}


@Composable
fun AppNav(startDestination: String = Routes.LOGIN, repository: PatientDataRepository) {
    val nav = rememberNavController()

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
                onLoginAsPatient = { userName -> nav.navigate(Routes.patientHomeRoute(userName)) },
                onLoginAsProfessional = { nav.navigate(Routes.PROFESSIONAL_HOME) },
                onGoToRegister = { nav.navigate(Routes.REGISTER) },
                onLoginAsAdmin = { nav.navigate(Routes.ADMIN_HOME) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(onNavigateBack = { nav.popBackStack() })
        }

        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(nav)
        }

        composable(Routes.ADMIN_USER_LIST) {
            AdminUserListScreen(nav)
        }

        // --- NAVEGACIÓN DEL PACIENTE ---
        composable(
            route = Routes.PATIENT_HOME_ROUTE,
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            PatientHomeScreen(nav, userName)
        }
        composable(Routes.NOTES) { NotesScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.EMERGENCY) { EmergencyContactScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.PERSONAL) { PersonalDataScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.CALENDAR) { CalendarScreen(onBack = { nav.popBackStack() }) }
        composable(Routes.MEDS) { MedsReminderScreen(onBack = { nav.popBackStack() }) }

        composable(Routes.DRUG_INFO) {
            val viewModel: DrugInfoViewModel = viewModel(
                factory = ViewModelFactory(repository)
            )
            DrugInfoScreen(
                viewModel = viewModel,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.MAP) {
            MapScreen(
                onBack = { nav.popBackStack() }
            )
        }

        // --- NAVEGACIÓN DEL PROFESIONAL ---
        composable(Routes.PROFESSIONAL_HOME) { ProfessionalHomeScreen(nav) }
        composable(Routes.PROF_AGENDA) { ProfAgendaScreen(onBack = { nav.popBackStack() }) }

        composable(Routes.PROF_PATIENTS) {
            ProfPatientsScreen(
                onPatientClick = { patientId ->
                    nav.navigate(Routes.patientDetailRoute(patientId))
                },
                onBack = { nav.popBackStack() }
            )
        }

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
