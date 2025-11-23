package com.example.proyectoappmoviles.pantallasdesarrollador

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoappmoviles.databinding.ActivityPantallaInicioDesarrolladorBinding
import com.example.proyectoappmoviles.pantallasjugador.RankingJugadorActivity

class PantallaInicioDesarrolladorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaInicioDesarrolladorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaInicioDesarrolladorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddPregunta.setOnClickListener {
            val intent = Intent(this, NuevaPreguntaActivity::class.java)
            startActivity(intent)

        }

        binding.btnEliminarPregunta.setOnClickListener {
            val intent = Intent(this, EliminarPreguntasActivity::class.java)
            startActivity(intent)
        }

        binding.btnModificarPregunta.setOnClickListener {
            val intent = Intent(this, ModificarPreguntasActivity::class.java)
            startActivity(intent)
        }

        binding.btnGestionDevs.setOnClickListener {
            val intent = Intent(this, GestionarDesarrolladoresActivity::class.java)
            startActivity(intent)

        }

        binding.btnOpcionesDev.setOnClickListener {
            val intent = Intent(this, CuestionarioConfiguracionActivity::class.java)
            startActivity(intent)

        }
    }
}

