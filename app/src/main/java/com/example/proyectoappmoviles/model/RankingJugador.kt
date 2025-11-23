package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class RankingJugador(
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("puntaje_total")
    val puntaje_total: Int,

    @SerializedName("cuestionariosJugados")
    val cuestionariosJugados: Int
)
