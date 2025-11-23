package com.example.proyectoappmoviles.pantallasdesarrollador

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.databinding.ActivityNuevaPreguntaBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.model.EditarPregunta
import com.example.proyectoappmoviles.model.Pregunta
import com.example.proyectoappmoviles.service.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EditarPreguntasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevaPreguntaBinding
    private val gson = Gson()
    private var preguntaActual: Pregunta? = null

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

        val json = intent.getStringExtra("preguntaJson")
        if (!json.isNullOrEmpty()) {
            preguntaActual = gson.fromJson(json, Pregunta::class.java)
            cargarEnCampos()
        }

        binding.btnGuardarPregunta.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarEnCampos() {
        preguntaActual?.let { p ->
            binding.etPregunta.setText(p.pregunta)
            binding.etOpcion1.setText(p.opcion1)
            binding.etOpcion2.setText(p.opcion2)
            binding.etOpcion3.setText(p.opcion3)
            when (p.correcta) {
                1 -> binding.rbOpcion1.isChecked = true
                2 -> binding.rbOpcion2.isChecked = true
                3 -> binding.rbOpcion3.isChecked = true
            }
        }
    }

    private fun guardarCambios() {
        val p = preguntaActual ?: return
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

        val req = EditarPregunta(
            id = p.id,
            pregunta = preguntaTxt,
            opcion1 = op1,
            opcion2 = op2,
            opcion3 = op3,
            correcta = correcta
        )

        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.webService.editarPregunta(req)
                if (resp.mensaje != null && resp.mensaje.contains("actualizada", ignoreCase = true)) {
                    mostrarMensaje("Pregunta actualizada correctamente")
                } else {
                    mostrarError(resp.mensaje ?: "Respuesta inesperada")
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