package cl.tuusuario.healing.ui.screens.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.tuusuario.healing.data.local.remote.openfda.DrugLabelItem
import cl.tuusuario.healing.data.local.repository.PatientDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DrugInfoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val drugItem: DrugLabelItem? = null
)

class DrugInfoViewModel(
    private val repository: PatientDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DrugInfoUiState())
    val uiState: StateFlow<DrugInfoUiState> = _uiState

    fun searchDrug(genericName: String) {
        if (genericName.isBlank()) {
            _uiState.value = DrugInfoUiState(error = "Ingresa un nombre de medicamento.")
            return
        }

        viewModelScope.launch {
            _uiState.value = DrugInfoUiState(isLoading = true)
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getDrugInfoFromFda(genericName)
                }
                if (result == null) {
                    _uiState.value = DrugInfoUiState(
                        error = "No se encontró información para ese medicamento."
                    )
                } else {
                    _uiState.value = DrugInfoUiState(
                        drugItem = result
                    )
                }
            } catch (e: Exception) {
                Log.e("DrugInfoViewModel", "Error consultando openFDA", e)
                _uiState.value = DrugInfoUiState(
                    error = "Ocurrió un error al consultar la FDA. Intenta nuevamente."
                )
            }
        }
    }
}