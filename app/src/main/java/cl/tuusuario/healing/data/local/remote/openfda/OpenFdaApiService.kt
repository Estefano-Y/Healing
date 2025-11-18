package cl.tuusuario.healing.data.local.remote.openfda

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApiService {

    // Ejemplo de endpoint:
    // https://api.fda.gov/drug/label.json?search=openfda.generic_name:ibuprofen&limit=1
    @GET("drug/label.json")
    suspend fun searchDrugLabel(
        @Query("search") search: String,
        @Query("limit") limit: Int = 1
    ): DrugLabelResponse
}