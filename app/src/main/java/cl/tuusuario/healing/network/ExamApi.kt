package cl.tuusuario.healing.network

import cl.tuusuario.healing.data.local.remote.Exam
import retrofit2.http.*

interface ExamApi {

    @POST("api/exams")
    suspend fun uploadExam(@Body exam: Exam): Exam

    @GET("api/exams/patient/{id}")
    suspend fun getExamsByPatient(@Path("id") id: Long): List<Exam>
}