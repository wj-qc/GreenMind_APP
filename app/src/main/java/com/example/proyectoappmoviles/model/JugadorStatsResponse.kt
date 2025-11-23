package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class JugadorStatsResponse(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("preguntas_correctas")
    val preguntasCorrectas: Int,

    @SerializedName("preguntas_incorrectas")
    val preguntasIncorrectas: Int,

    @SerializedName("cuestionarios_aprobados")
    val cuestionariosAprobados: Int,

    @SerializedName("cuestionarios_desaprobados")
    val cuestionariosDesaprobados: Int,

    @SerializedName("puntaje_total")
    val puntajeTotal: Int,

    @SerializedName("puesto")
    val puesto: Int
)
