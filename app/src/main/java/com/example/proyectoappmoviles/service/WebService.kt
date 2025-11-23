package com.example.proyectoappmoviles.service

import com.example.proyectoappmoviles.model.Desarrollador
import com.example.proyectoappmoviles.model.DesarrolladoresResponde
import com.example.proyectoappmoviles.model.EditarPregunta
import com.example.proyectoappmoviles.model.EliminarPregunta
import com.example.proyectoappmoviles.model.Jugador
import com.example.proyectoappmoviles.model.JugadorStatsResponse
import com.example.proyectoappmoviles.model.LoginResponse
import com.example.proyectoappmoviles.model.MensajeResponse
import com.example.proyectoappmoviles.model.NuevaPregunta
import com.example.proyectoappmoviles.model.PreguntaResponse
import com.example.proyectoappmoviles.model.RankingResponse
import com.example.proyectoappmoviles.model.Resultado
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object AppConstantes {
    const val URL_BASE = "http://192.168.1.35:3000/"
}

interface WebService {

    @POST("login")
    suspend fun loginUnificado(@Body credenciales: Jugador): LoginResponse

    @POST("jugador/registrar")
    suspend fun registrarJugador(@Body jugador: Jugador): MensajeResponse
    
    @GET("preguntas")
    suspend fun getPreguntas(): PreguntaResponse

    @GET("ranking")
    suspend fun getRanking(): RankingResponse

    @POST("jugador/resultado")
    suspend fun enviarResultado(@Body resultado: Resultado): MensajeResponse

    @GET("jugador/{id}")
    suspend fun getEstadisticasJugador(@Path("id") jugadorId: Int): JugadorStatsResponse

    //Desarrolladores
    @POST("pregunta/agregar")
    suspend fun agregarPregunta(@Body pregunta: NuevaPregunta): MensajeResponse

    @POST("pregunta/editar")
    suspend fun editarPregunta(@Body pregunta: EditarPregunta): MensajeResponse

    @POST("pregunta/eliminar")
    suspend fun eliminarPregunta(@Body request: EliminarPregunta): MensajeResponse

    @GET("desarrolladores")
    suspend fun getDesarrolladores(): Response<DesarrolladoresResponde>

    @DELETE("/desarrolladores/eliminar/{id}")
    suspend fun eliminarDesarrollador(@Path(value = "id")id: Int): Response<String>

    @POST("/desarrolladores/agregar")
    suspend fun agregarDesarrollador(@Body desarrollador: Desarrollador): MensajeResponse
}

object RetrofitClient {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}
