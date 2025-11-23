package com.example.proyectoappmoviles.pantallasjugador

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoappmoviles.R
import com.example.proyectoappmoviles.databinding.ActivityConfiguracionJugadorBinding

class ConfiguracionJugadorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionJugadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionJugadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("config_juego", Context.MODE_PRIVATE)

        val contraste = prefs.getBoolean("contraste", false)
        val tamanoTexto = prefs.getInt("tamano_texto", 16)
        val silenciar = prefs.getBoolean("silenciar", false)

        binding.switchContraste.isChecked = contraste
        binding.seekBarTamanoTexto.progress = tamanoTexto
        binding.switchSilenciar.isChecked = silenciar

        aplicarConfiguracion(contraste, tamanoTexto)

        binding.btnGuardarConfig.setOnClickListener {
            val editor = prefs.edit()
            editor.putBoolean("contraste", binding.switchContraste.isChecked)
            editor.putInt("tamano_texto", binding.seekBarTamanoTexto.progress)
            editor.putBoolean("silenciar", binding.switchSilenciar.isChecked)
            editor.apply()
            finish()
        }


        binding.switchContraste.setOnCheckedChangeListener { _, _ ->
            aplicarConfiguracion(
                binding.switchContraste.isChecked,
                binding.seekBarTamanoTexto.progress
            )
        }

        binding.seekBarTamanoTexto.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                aplicarConfiguracion(
                    binding.switchContraste.isChecked,
                    progress
                )
            }
            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })
    }

    private fun aplicarConfiguracion(contraste: Boolean, tamanoTexto: Int) {
        binding.root.alpha = if (contraste) 0.7f else 1.0f
        aplicarTexto(tamanoTexto)
    }

    private fun aplicarTexto(tamanoTexto: Int) {
        binding.tvTituloConfig.textSize = tamanoTexto.toFloat()
        binding.tvContraste.textSize = tamanoTexto.toFloat()
        binding.tvTamanoTexto.textSize = tamanoTexto.toFloat()
        binding.tvSilenciar.textSize = tamanoTexto.toFloat()
    }


}