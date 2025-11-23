package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class PreguntaResponse(
    @SerializedName("listaPreguntas")
    val preguntas: List<Pregunta>
)
