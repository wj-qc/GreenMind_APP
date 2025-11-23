package com.example.proyectoappmoviles.pantallasdesarrollador

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.databinding.ActivityCuestionarioConfiguracionBinding
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class CuestionarioConfiguracionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuestionarioConfiguracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuestionarioConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cargarConfiguracionActual()

        binding.btnGuardarConfiguracionCuestionario.setOnClickListener {
            guardarConfiguracion()
        }
    }

    private fun cargarConfiguracionActual() {
        val prefs = getSharedPreferences("config_cuestionario", Context.MODE_PRIVATE)
        val cantidad = prefs.getInt("cantidad_preguntas", 5)
        val segundos = prefs.getInt("segundos_por_pregunta", 10)

        binding.etCantidadPreguntas.setText(cantidad.toString())
        binding.etSegundosPorPregunta.setText(segundos.toString())
    }

    private fun guardarConfiguracion() {
        val cantidadStr = binding.etCantidadPreguntas.text.toString()
        val segundosStr = binding.etSegundosPorPregunta.text.toString()

        if (cantidadStr.isEmpty() || segundosStr.isEmpty()) {
            mostrarError("Ambos campos son requeridos.")
            return
        }

        val cantidad = cantidadStr.toIntOrNull() ?: 0
        val segundos = segundosStr.toIntOrNull() ?: 0

        if (cantidad <= 0 || segundos <= 0) {
            mostrarError("Los valores deben ser mayores a cero.")
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.getPreguntas()
                val totalPreguntas = response.preguntas.size

                if (cantidad > totalPreguntas) {
                    mostrarError("La cantidad no puede ser mayor al número de preguntas existentes ($totalPreguntas).")
                    return@launch
                }

                val prefs = getSharedPreferences("config_cuestionario", Context.MODE_PRIVATE).edit()
                prefs.putInt("cantidad_preguntas", cantidad)
                prefs.putInt("segundos_por_pregunta", segundos)
                prefs.apply()

                Toast.makeText(this@CuestionarioConfiguracionActivity, "Configuración guardada", Toast.LENGTH_SHORT).show()
                finish()

            } catch (e: Exception) {
                mostrarError("Error de conexión: " + e.message)
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error de Validación")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
