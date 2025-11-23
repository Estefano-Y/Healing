package cl.tuusuario.healing.network

import cl.tuusuario.healing.data.local.remote.Appointment
import retrofit2.http.*

interface AppointmentApi {

    @GET("api/appointments")
    suspend fun getAppointments(): List<Appointment>

    @GET("api/appointments/{id}")
    suspend fun getAppointmentById(@Path("id") id: Long): Appointment

    @POST("api/appointments")
    suspend fun createAppointment(@Body appointment: Appointment): Appointment

    @PUT("api/appointments/{id}")
    suspend fun updateAppointment(
        @Path("id") id: Long,
        @Body appointment: Appointment
    ): Appointment

    @DELETE("api/appointments/{id}")
    suspend fun deleteAppointment(@Path("id") id: Long)
}