package com.example.proyectoappmoviles.model

data class NuevaPregunta(
    val pregunta: String,
    val opcion1: String?,
    val opcion2: String?,
    val opcion3: String?,
    val correcta: Int
)
