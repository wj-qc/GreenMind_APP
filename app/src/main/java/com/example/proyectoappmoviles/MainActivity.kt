package com.example.proyectoappmoviles

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoappmoviles.databinding.ActivityMainBinding
import com.example.proyectoappmoviles.sesiones.LogInJugador
import com.example.proyectoappmoviles.sesiones.RegistrarJugadorActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarJugador.setOnClickListener {
            startActivity(Intent(this, RegistrarJugadorActivity::class.java))
        }

        binding.btnLoginJugador.setOnClickListener {
            startActivity(Intent(this, LogInJugador::class.java))
        }
    }
}
