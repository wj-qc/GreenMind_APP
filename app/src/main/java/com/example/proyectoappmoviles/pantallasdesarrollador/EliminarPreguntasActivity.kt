package com.example.proyectoappmoviles.pantallasdesarrollador

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappmoviles.databinding.ActivityEliminarPreguntasBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.helpers.EliminarPreguntasAdapter
import com.example.proyectoappmoviles.model.EliminarPregunta
import com.example.proyectoappmoviles.model.Pregunta
import com.example.proyectoappmoviles.model.PreguntaResponse
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class EliminarPreguntasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEliminarPreguntasBinding
    private var listaPreguntas = listOf<Pregunta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEliminarPreguntasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(binding.tvTituloEliminar)
        )

        binding.rvPreguntasEliminar.layoutManager = LinearLayoutManager(this)
        cargarPreguntas()
    }

    private fun cargarPreguntas() {
        lifecycleScope.launch {
            try {
                val resp: PreguntaResponse = RetrofitClient.webService.getPreguntas()
                listaPreguntas = resp.preguntas
                binding.rvPreguntasEliminar.adapter = EliminarPreguntasAdapter(listaPreguntas) { pregunta ->
                    mostrarConfirmacion(pregunta)
                }
            } catch (e: Exception) {
                mostrarMensaje("Error al cargar preguntas: ${e.message}")
            }
        }
    }

    private fun mostrarConfirmacion(p: Pregunta) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Seguro que deseas eliminar la pregunta?\n\n${p.pregunta}")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarPregunta(p.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarPregunta(id: Int) {
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.webService.eliminarPregunta(EliminarPregunta(id))
                if (resp.mensaje?.contains("eliminada", ignoreCase = true) == true) {
                    mostrarMensaje("Pregunta eliminada correctamente")
                    cargarPreguntas()
                } else {
                    mostrarMensaje(resp.mensaje ?: "No se pudo eliminar")
                }
            } catch (e: Exception) {
                mostrarMensaje("Error de conexión: ${e.message}")
            }
        }
    }

    private fun mostrarMensaje(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Información")
            .setMessage(msg)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
