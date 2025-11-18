package cl.tuusuario.healing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cl.tuusuario.healing.data.local.AppDatabase
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import cl.tuusuario.healing.ui.navigation.AppNav
import cl.tuusuario.healing.ui.theme.HealingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = PatientDataRepository(
            noteDao = database.noteDao(),
            personalDataDao = database.personalDataDao(),
            emergencyContactDao = database.emergencyContactDao(),
            medsReminderDao = database.medsReminderDao(),
            professionalDao = database.professionalDao(),
            userDao = database.userDao()
        )

        setContent {
            HealingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNav(repository = repository)
                }
            }
        }
    }
}