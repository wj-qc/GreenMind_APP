package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class DesarrolladoresResponde(
    @SerializedName("listaDesarrolladores")
    var listadesarrolladores: ArrayList<Desarrollador>
)
