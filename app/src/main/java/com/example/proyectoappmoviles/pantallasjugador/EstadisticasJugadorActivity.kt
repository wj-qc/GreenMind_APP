package com.example.proyectoappmoviles.pantallasjugador

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.databinding.ActivityEstadisticasJugadorBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class EstadisticasJugadorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstadisticasJugadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadisticasJugadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(
                binding.tvPreguntasCorrectas,
                binding.tvPreguntasIncorrectas,
                binding.tvCuestionariosAprobados,
                binding.tvCuestionariosDesaprobados,
                binding.tvPuntajeGlobal,
                binding.tvPuesto,
                binding.tvTituloEstadisticas
            )
        )

        val jugadorId = intent.getIntExtra("jugadorId", -1)
        val nombreJugador = intent.getStringExtra("nombreJugador") ?: "Jugador"
        binding.tvTituloEstadisticas.text = "Estadísticas de $nombreJugador"

        if (jugadorId != -1) {
            cargarEstadisticas(jugadorId)
        } else {
            mostrarError("No se pudo obtener el ID del jugador.")
        }
    }

    private fun cargarEstadisticas(jugadorId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.getEstadisticasJugador(jugadorId)

                binding.tvPreguntasCorrectas.text = "Preg. correctas: ${response.preguntasCorrectas}"
                binding.tvPreguntasIncorrectas.text = "Preg. incorrectas: ${response.preguntasIncorrectas}"
                binding.tvCuestionariosAprobados.text = "Cuest. aprobados: ${response.cuestionariosAprobados}"
                binding.tvCuestionariosDesaprobados.text = "Cuest. desaprobados: ${response.cuestionariosDesaprobados}"
                binding.tvPuntajeGlobal.text = "Puntaje global: ${response.puntajeTotal}"
                binding.tvPuesto.text = "Puesto: ${response.puesto}"

            } catch (e: Exception) {
                mostrarError("No se pudieron cargar las estadísticas. Inténtalo de nuevo más tarde.")
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }
}
