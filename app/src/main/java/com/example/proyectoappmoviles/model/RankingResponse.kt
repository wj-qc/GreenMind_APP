package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class RankingResponse(
    @SerializedName("listaRanking")
    val ranking: List<RankingJugador>
)
