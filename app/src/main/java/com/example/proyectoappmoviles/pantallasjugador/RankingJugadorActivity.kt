package com.example.proyectoappmoviles.pantallasjugador

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappmoviles.databinding.ActivityRankingJugadorBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.helpers.RankingAdapter
import com.example.proyectoappmoviles.model.RankingJugador
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.launch

class RankingJugadorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingJugadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingJugadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(
                binding.tvTituloRanking
            )
        )

        binding.rvRanking.layoutManager = LinearLayoutManager(this)
        cargarRanking()
    }

    private fun cargarRanking() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.getRanking()
                binding.rvRanking.adapter = RankingAdapter(response.ranking)
            } catch (e: Exception) {
                mostrarError("No se pudo cargar el ranking. Inténtalo de nuevo más tarde.")
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ -> finish() }
            .setCancelable(false)
            .create()
            .show()
    }
}
