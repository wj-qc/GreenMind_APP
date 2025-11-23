package com.example.proyectoappmoviles.sesiones

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.databinding.ActivityLogInJugadorBinding
import com.example.proyectoappmoviles.model.Jugador
import com.example.proyectoappmoviles.pantallasdesarrollador.PantallaInicioDesarrolladorActivity
import com.example.proyectoappmoviles.pantallasjugador.PantallaInicioJugadorActivity
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LogInJugador : AppCompatActivity() {

    private lateinit var binding: ActivityLogInJugadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInJugadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginJugador.setOnClickListener {
            loginUnificado()
        }
    }

    private fun loginUnificado() {
        val nombre = binding.etNombreLogin.text.toString().trim()
        val pass = binding.etPasswordLogin.text.toString().trim()

        if (nombre.isEmpty() || pass.isEmpty()) {
            mostrarMensaje("Completa todos los campos")
            return
        }

        lifecycleScope.launch {
            try {
                val credenciales = Jugador(nombre = nombre, password = pass)
                val response = RetrofitClient.webService.loginUnificado(credenciales)

                if (response.ok == true) {
                    when (response.tipoUsuario) {
                        "developer" -> {
                            val intent = Intent(this@LogInJugador, PantallaInicioDesarrolladorActivity::class.java)
                            intent.putExtra("nombreDesarrollador", response.nombre)
                            startActivity(intent)
                            finish()
                        }
                        "player" -> {
                            val intent = Intent(this@LogInJugador, PantallaInicioJugadorActivity::class.java)
                            intent.putExtra("nombreJugador", response.nombre)
                            intent.putExtra("jugadorId", response.id)
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            mostrarMensaje("Tipo de usuario desconocido.")
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    mostrarMensaje("Usuario o contraseña incorrectos")
                } else {
                    mostrarMensaje("Error de conexión. Inténtalo de nuevo.")
                }
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
