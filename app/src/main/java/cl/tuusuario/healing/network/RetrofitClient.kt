package cl.tuusuario.healing.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // emulador
    const val BASE_URL = "http://10.0.2.2:8080/"

    // celular físico
    // const val BASE_URL = "http://TU_IP_LOCAL:8080/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val patientApi: PatientApi = retrofit.create(PatientApi::class.java)
    val medicineApi: MedicineApi = retrofit.create(MedicineApi::class.java)
    val examApi: ExamApi = retrofit.create(ExamApi::class.java)
    val appointmentApi: AppointmentApi = retrofit.create(AppointmentApi::class.java)
}