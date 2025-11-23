package com.example.proyectoappmoviles.helpers

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.proyectoappmoviles.R

object ConfHelper {
    fun aplicarConfigGlobal(root: View, context: Context, vistas: List<View>) {
        val prefs = context.getSharedPreferences("config_juego", Context.MODE_PRIVATE)
        val contraste = prefs.getBoolean("contraste", false)
        val tamanoTexto = prefs.getInt("tamano_texto", 16)


        root.alpha = if (contraste) 0.7f else 1.0f

        vistas.forEach {
            if (it is TextView) {
                it.textSize = tamanoTexto.toFloat()
            }
        }
    }
}
