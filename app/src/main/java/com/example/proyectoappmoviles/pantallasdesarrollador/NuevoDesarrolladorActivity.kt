package com.example.proyectoappmoviles.pantallasdesarrollador

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.R
import com.example.proyectoappmoviles.databinding.ActivityNuevoDesarrolladorBinding
import com.example.proyectoappmoviles.model.Desarrollador
import com.example.proyectoappmoviles.model.MensajeResponse
import com.example.proyectoappmoviles.service.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NuevoDesarrolladorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevoDesarrolladorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevoDesarrolladorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        asignarReferencias()
    }

    private fun asignarReferencias() {
        binding.btnRegistrarDev.setOnClickListener {
            registrar()
        }
    }

    private fun registrar() {
        val nombre = binding.etNombreDev.text.toString().trim()
        val pass = binding.etPassDev.text.toString().trim()
        val passConf = binding.etPassDevConf.text.toString().trim()

        if (nombre.isEmpty() || pass.isEmpty() || passConf.isEmpty()) {
            mostrarMensaje("Por favor, completa todos los campos.")
            return
        }

        if (pass != passConf) {
            mostrarMensaje("Las contraseñas no coinciden.")
            return
        }

        val desarrollador = Desarrollador(nombre = nombre, password = pass)

        lifecycleScope.launch {
            try {
                val rpta = RetrofitClient.webService.agregarDesarrollador(desarrollador)
                mostrarMensaje2(rpta.mensaje)

            } catch (e: Exception) {
                var msg = "Error de conexión. Inténtalo de nuevo."
                if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, MensajeResponse::class.java)
                            msg = errorResponse.mensaje
                        } catch (_: Exception) {
                            msg = "Error ${e.code()}: Respuesta inesperada del servidor."
                        }
                    }
                }

                mostrarMensaje(msg)
            }
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Información")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }

    private fun mostrarMensaje2(mensaje: String) {
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