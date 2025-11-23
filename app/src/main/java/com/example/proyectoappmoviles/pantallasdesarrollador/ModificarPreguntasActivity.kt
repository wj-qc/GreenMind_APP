package com.example.proyectoappmoviles.pantallasdesarrollador

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappmoviles.databinding.ActivityModificarPreguntasBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.helpers.ModificarPreguntasAdapter
import com.example.proyectoappmoviles.model.Pregunta
import com.example.proyectoappmoviles.model.PreguntaResponse
import com.example.proyectoappmoviles.service.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ModificarPreguntasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarPreguntasBinding
    private val gson = Gson()
    private var listaPreguntas = listOf<Pregunta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarPreguntasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(binding.tvTituloModificar)
        )

        binding.rvPreguntasModificar.layoutManager = LinearLayoutManager(this)

        cargarLista()
    }

    private fun cargarLista() {
        lifecycleScope.launch {
            try {
                val resp: PreguntaResponse = RetrofitClient.webService.getPreguntas()
                listaPreguntas = resp.preguntas
                binding.rvPreguntasModificar.adapter = ModificarPreguntasAdapter(listaPreguntas) { pregunta ->
                    val json = gson.toJson(pregunta)
                    val intent = Intent(this@ModificarPreguntasActivity, EditarPreguntasActivity::class.java)
                    intent.putExtra("preguntaJson", json)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                mostrarMensaje("Error al cargar preguntas: " + e.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLista()
    }

    private fun mostrarMensaje(msg: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Información")
            .setMessage(msg)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
