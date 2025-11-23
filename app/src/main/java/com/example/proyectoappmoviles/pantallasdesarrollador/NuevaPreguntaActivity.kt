package com.example.proyectoappmoviles.pantallasdesarrollador

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.databinding.ActivityNuevaPreguntaBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.model.MensajeResponse
import com.example.proyectoappmoviles.model.NuevaPregunta
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class NuevaPreguntaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevaPreguntaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaPreguntaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(
                binding.tvTituloAgregar,
                binding.etPregunta,
                binding.etOpcion1,
                binding.etOpcion2,
                binding.etOpcion3,
                binding.tvSeleccionCorrecta,
                binding.rbOpcion1,
                binding.rbOpcion2,
                binding.rbOpcion3,
                binding.btnGuardarPregunta
            )
        )

        binding.btnGuardarPregunta.setOnClickListener {
            guardarPregunta()
        }
    }

    private fun guardarPregunta() {
        val preguntaTxt = binding.etPregunta.text.toString().trim()
        val op1 = binding.etOpcion1.text.toString().trim()
        val op2 = binding.etOpcion2.text.toString().trim()
        val op3 = binding.etOpcion3.text.toString().trim()

        if (preguntaTxt.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty()) {
            mostrarError("Completa todos los campos")
            return
        }

        val correcta = when (binding.rgCorrecta.checkedRadioButtonId) {
            binding.rbOpcion1.id -> 1
            binding.rbOpcion2.id -> 2
            binding.rbOpcion3.id -> 3
            else -> 0
        }

        if (correcta !in 1..3) {
            mostrarError("Selecciona la opción correcta")
            return
        }

        binding.btnGuardarPregunta.isEnabled = false

        val nueva = NuevaPregunta(
            pregunta = preguntaTxt,
            opcion1 = op1,
            opcion2 = op2,
            opcion3 = op3,
            correcta = correcta
        )

        lifecycleScope.launch {
            try {
                val resp: MensajeResponse = RetrofitClient.webService.agregarPregunta(nueva)
                if (resp.mensaje != null && resp.mensaje.contains("registrada", ignoreCase = true)) {
                    mostrarMensaje("Pregunta agregada correctamente")
                } else {
                    mostrarError(resp.mensaje ?: "Respuesta inesperada del servidor")
                }
            } catch (e: Exception) {
                mostrarError("Error de conexión: ${e.message}")
            } finally {
                binding.btnGuardarPregunta.isEnabled = true
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Información")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mostrarMensaje(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Éxito")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}