package com.example.proyectoappmoviles.sesiones

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.databinding.ActivityRegistrarJugadorBinding
import com.example.proyectoappmoviles.model.Jugador
import com.example.proyectoappmoviles.pantallasjugador.PantallaInicioJugadorActivity
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class RegistrarJugadorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarJugadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarJugadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            registrarJugador()
        }
    }

    private fun registrarJugador() {
        val nombre = binding.etNombre.text.toString().trim()
        val pass = binding.etPassword.text.toString().trim()
        val confirm = binding.etConfirmar.text.toString().trim()

        if (nombre.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            mostrarMensaje("Completa todos los campos")
            return
        }

        if (pass != confirm) {
            mostrarMensaje("Las contraseñas no coinciden")
            return
        }

        lifecycleScope.launch {
            try {
                val nuevoJugador = Jugador(nombre = nombre, password = pass)
                val responseRegistro = RetrofitClient.webService.registrarJugador(nuevoJugador)

                if (responseRegistro.mensaje == "Jugador registrado correctamente") {

                    val responseLogin = RetrofitClient.webService.loginUnificado(nuevoJugador)

                    if (responseLogin.ok == true && responseLogin.tipoUsuario == "player") {
                        val intent = Intent(this@RegistrarJugadorActivity, PantallaInicioJugadorActivity::class.java)
                        intent.putExtra("nombreJugador", responseLogin.nombre)
                        intent.putExtra("jugadorId", responseLogin.id)
                        startActivity(intent)
                        finish()
                    } else {
                        mostrarMensaje("Registro exitoso, pero no se pudo iniciar sesión. Inténtalo manualmente.")
                    }
                } else {
                    mostrarMensaje(responseRegistro.mensaje)
                }
            } catch (e: Exception) {
                mostrarMensaje("Error de conexión. Inténtalo de nuevo más tarde.")
            }
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
        ventana.create().show()
    }
}
