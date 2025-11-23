package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class Resultado(
    @SerializedName("jugador_id")
    val jugadorId: Int,

    @SerializedName("correctas")
    val correctas: Int = 0,

    @SerializedName("incorrectas")
    val incorrectas: Int = 0,

    @SerializedName("aprobados")
    val aprobados: Int = 0,

    @SerializedName("desaprobados")
    val desaprobados: Int = 0,

    @SerializedName("puntaje_delta")
    val puntajeDelta: Int = 0
)
