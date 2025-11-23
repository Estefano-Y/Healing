package cl.tuusuario.healing.network

import cl.tuusuario.healing.data.local.remote.Patient
import retrofit2.http.*

interface PatientApi {

    @GET("api/patients")
    suspend fun getPatients(): List<Patient>

    @GET("api/patients/{id}")
    suspend fun getPatientById(@Path("id") id: Long): Patient

    @POST("api/patients")
    suspend fun createPatient(@Body patient: Patient): Patient

    @PUT("api/patients/{id}")
    suspend fun updatePatient(
        @Path("id") id: Long,
        @Body patient: Patient
    ): Patient

    @DELETE("api/patients/{id}")
    suspend fun deletePatient(@Path("id") id: Long)
}