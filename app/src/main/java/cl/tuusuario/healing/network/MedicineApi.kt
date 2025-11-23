package cl.tuusuario.healing.network

import cl.tuusuario.healing.data.local.remote.Medicine
import retrofit2.http.*

interface MedicineApi {

    @GET("api/medicines")
    suspend fun getMedicines(): List<Medicine>

    @GET("api/medicines/search")
    suspend fun searchMedicines(@Query("name") name: String): List<Medicine>

    @POST("api/medicines")
    suspend fun createMedicine(@Body medicine: Medicine): Medicine
}