package com.example.proyectoappmoviles.pantallasjugador

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoappmoviles.databinding.ActivityPantallaInicioJugadorBinding
import com.example.proyectoappmoviles.helpers.ConfHelper

class PantallaInicioJugadorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaInicioJugadorBinding
    private var jugadorId: Int = -1
    private var nombreJugador: String = "Jugador"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaInicioJugadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nombreJugador = intent.getStringExtra("nombreJugador") ?: "Jugador"
        jugadorId = intent.getIntExtra("jugadorId", -1)

        binding.tvBienvenida.text = "Bienvenido, $nombreJugador!"

        binding.btnEstadisticas.setOnClickListener {
            val intent = Intent(this, EstadisticasJugadorActivity::class.java)
            intent.putExtra("nombreJugador", nombreJugador)
            intent.putExtra("jugadorId", jugadorId) // <--- ¡CAMBIO AÑADIDO!
            startActivity(intent)
        }

        binding.btnJugar.setOnClickListener {
            val intent = Intent(this, CuestionarioActivity::class.java)
            intent.putExtra("nombreJugador", nombreJugador)
            intent.putExtra("jugadorId", jugadorId)
            startActivity(intent)
        }

        binding.btnOpciones.setOnClickListener {
            val intent = Intent(this, ConfiguracionJugadorActivity::class.java)
            startActivity(intent)
        }

        binding.btnRanking.setOnClickListener {
            val intent = Intent(this, RankingJugadorActivity::class.java)
            startActivity(intent)
        }

        aplicarConfig()
    }

    override fun onResume() {
        super.onResume()
        aplicarConfig()
    }

    private fun aplicarConfig() {
        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(
                binding.tvBienvenida,
                binding.btnEstadisticas,
                binding.btnJugar,
                binding.btnOpciones,
                binding.btnRanking
            )
        )
    }
}
