package com.example.proyectoappmoviles.pantallasdesarrollador

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappmoviles.databinding.ActivityGestionarDesarrolladoresBinding
import com.example.proyectoappmoviles.helpers.ConfHelper
import com.example.proyectoappmoviles.helpers.Adaptador
import com.example.proyectoappmoviles.model.Desarrollador
import com.example.proyectoappmoviles.service.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GestionarDesarrolladoresActivity : AppCompatActivity() {

    private var listaDesarrolladores = ArrayList<Desarrollador>()
    private lateinit var binding: ActivityGestionarDesarrolladoresBinding
    private var adaptador = Adaptador()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionarDesarrolladoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ConfHelper.aplicarConfigGlobal(
            root = binding.root,
            context = this,
            vistas = listOf(binding.tvTituloDesarrolladores)
        )

        asignarReferencias()
    }

    override fun onResume() {
        super.onResume()
        cargarDesarrolladores()
    }

    private fun asignarReferencias() {
        binding.rvDesarrolladores.layoutManager = LinearLayoutManager(this)
        binding.rvDesarrolladores.adapter = adaptador // Asignamos el adaptador una sola vez
        adaptador.setContexto(this)

        binding.btnNuevoDev.setOnClickListener {
            val intent = Intent(this, NuevoDesarrolladorActivity::class.java)
            startActivity(intent)
        }

        adaptador.setEliminarItem {
            eliminar(it)
        }
    }

    private fun eliminar(desarrollador: Desarrollador) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Eliminar")
        ventana.setMessage("¿Desea eliminar el desarrollador: ${desarrollador.nombre}?")
        ventana.setPositiveButton("Aceptar") { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val rpta = RetrofitClient.webService.eliminarDesarrollador(desarrollador.id)
                runOnUiThread {
                    if (rpta.isSuccessful) {
                        mostrarMensaje(rpta.body().toString())
                        cargarDesarrolladores()
                    }
                }
            }
        }
        ventana.setNegativeButton("Cancelar", null)
        ventana.create().show()
    }

    private fun cargarDesarrolladores() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.getDesarrolladores()

            runOnUiThread {
                if (rpta.isSuccessful) {
                    val desarrolladores = rpta.body()?.listadesarrolladores ?: emptyList()
                    listaDesarrolladores.clear()
                    listaDesarrolladores.addAll(desarrolladores)
                    adaptador.setListaDesarrolladores(listaDesarrolladores)
                    adaptador.notifyDataSetChanged()
                } else {
                    mostrarMensaje("Error al cargar: ${rpta.code()}")
                }
            }
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("Información")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}