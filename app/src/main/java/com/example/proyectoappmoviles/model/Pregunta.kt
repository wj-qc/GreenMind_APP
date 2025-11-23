package com.example.proyectoappmoviles.model

data class Pregunta(
    val id: Int,
    val pregunta: String,
    val opcion1: String,
    val opcion2: String,
    val opcion3: String,
    val correcta: Int
)