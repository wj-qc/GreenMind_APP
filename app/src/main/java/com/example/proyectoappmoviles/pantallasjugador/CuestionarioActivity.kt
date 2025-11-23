package com.example.proyectoappmoviles.pantallasjugador

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proyectoappmoviles.R
import com.example.proyectoappmoviles.databinding.ActivityCuestionarioBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.model.Pregunta
import com.example.proyectoappmoviles.model.Resultado
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class CuestionarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuestionarioBinding

    private var preguntas = mutableListOf<Pregunta>()
    private var resultadosDetallados = mutableListOf<String>()
    private var index = 0
    private var puntaje = 0
    private var correctas = 0
    private var incorrectas = 0
    private var countDownTimer: CountDownTimer? = null
    private var jugadorId: Int = -1
    private var tiempoTotal = 10
    private var cantidadPreguntas = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuestionarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CORREGIDO: Leemos la configuración guardada
        val prefs = getSharedPreferences("config_cuestionario", Context.MODE_PRIVATE)
        cantidadPreguntas = prefs.getInt("cantidad_preguntas", 5)
        tiempoTotal = prefs.getInt("segundos_por_pregunta", 10)

        jugadorId = intent.getIntExtra("jugadorId", -1)

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(
                binding.tvTituloCuestionario,
                binding.rbOpcion1,
                binding.rbOpcion2,
                binding.rbOpcion3,
                binding.tvTiempo,
                binding.btnSiguiente
            )
        )

        binding.btnSiguiente.setOnClickListener {
            verificarRespuesta()
            siguientePregunta()
        }

        cargarPreguntas()
    }

    private fun cargarPreguntas() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.getPreguntas()
                val preguntasAleatorias = response.preguntas.shuffled()
                preguntas.clear()
                preguntas.addAll(preguntasAleatorias.take(cantidadPreguntas))
                
                iniciarCuestionario()
            } catch (e: Exception) {
                mostrarErrorCarga("No se pudieron cargar las preguntas. Inténtelo de nuevo.")
            }
        }
    }

    private fun iniciarCuestionario() {
        countDownTimer?.cancel()
        index = 0
        puntaje = 0
        correctas = 0
        incorrectas = 0
        resultadosDetallados.clear()
        mostrarPregunta()
    }

    private fun mostrarPregunta() {
        if (index >= preguntas.size) {
            mostrarResultados()
            return
        }
        val p = preguntas[index]
        binding.tvTituloCuestionario.text = p.pregunta
        binding.rbOpcion1.text = p.opcion1
        binding.rbOpcion2.text = p.opcion2
        binding.rbOpcion3.text = p.opcion3

        binding.rgOpciones.clearCheck()
        iniciarTemporizador()
    }

    private fun iniciarTemporizador() {
        countDownTimer?.cancel()
        binding.progressTiempo.max = tiempoTotal
        binding.progressTiempo.progress = tiempoTotal
        binding.tvTiempo.text = tiempoTotal.toString()

        countDownTimer = object : CountDownTimer((tiempoTotal * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val segundos = (millisUntilFinished / 1000).toInt()
                binding.progressTiempo.progress = segundos
                binding.tvTiempo.text = segundos.toString()
            }

            override fun onFinish() {
                verificarRespuesta()
                siguientePregunta()
            }
        }.start()
    }

    private fun verificarRespuesta() {
        countDownTimer?.cancel()
        val p = preguntas[index]
        val seleccion = when (binding.rgOpciones.checkedRadioButtonId) {
            R.id.rbOpcion1 -> 1
            R.id.rbOpcion2 -> 2
            R.id.rbOpcion3 -> 3
            else -> 0
        }

        if (seleccion == p.correcta) {
            puntaje += 20
            correctas++
            resultadosDetallados.add("${p.pregunta}: Correcta (20 puntos)")
        } else {
            incorrectas++
            resultadosDetallados.add("${p.pregunta}: Incorrecta (0 puntos)")
        }
    }

    private fun siguientePregunta() {
        index++
        mostrarPregunta()
    }

    private fun mostrarResultados() {
        if (jugadorId == -1) {
            Log.e("CuestionarioActivity", "El ID del jugador es inválido.")
            finish()
            return
        }

        val aprobado = puntaje >= 60
        val resultadoFinal = Resultado(
            jugadorId = jugadorId,
            correctas = correctas,
            incorrectas = incorrectas,
            aprobados = if (aprobado) 1 else 0,
            desaprobados = if (!aprobado) 1 else 0,
            puntajeDelta = puntaje
        )

        lifecycleScope.launch {
            try {
                RetrofitClient.webService.enviarResultado(resultadoFinal)
            } catch (e: Exception) {
                Log.e("CuestionarioActivity", "Error al enviar el resultado: ${e.message}")
            }
        }

        val titulo = if (aprobado) "¡Aprobaste!" else "Desaprobaste..."
        val mensaje = StringBuilder()
        for (resultado in resultadosDetallados) {
            mensaje.append(resultado).append("\n\n")
        }
        mensaje.append("Puntaje total: $puntaje")

        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje.toString())
            .setPositiveButton("Volver") { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun mostrarErrorCarga(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }
}
