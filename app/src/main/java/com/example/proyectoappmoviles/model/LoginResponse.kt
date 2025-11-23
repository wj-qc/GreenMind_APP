package com.example.proyectoappmoviles.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("mensaje")
    val mensaje: String? = null,

    @SerializedName("ok")
    val ok: Boolean? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("userType")
    val tipoUsuario: String? = null
)
