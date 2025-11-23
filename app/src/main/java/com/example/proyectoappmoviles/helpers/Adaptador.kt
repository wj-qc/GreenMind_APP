package com.example.proyectoappmoviles.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoappmoviles.R
import com.example.proyectoappmoviles.model.Desarrollador
import java.util.ArrayList

class Adaptador: RecyclerView.Adapter<Adaptador.VH>() {

    private var listaDesarrolladores = ArrayList<Desarrollador>()
    private lateinit var context: Context

    private var eliminarItem:((Desarrollador) -> Unit)? = null

    fun setEliminarItem(call : (Desarrollador) -> Unit) {
        this.eliminarItem = call
    }

    fun setContexto(context: Context) {
        this.context = context
    }

    fun setListaDesarrolladores(desarrolladores: ArrayList<Desarrollador>) {
        this.listaDesarrolladores = desarrolladores
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private var tvNombreDev: TextView = view.findViewById(R.id.tvNombreDev)
        private var tvIdDev: TextView = view.findViewById(R.id.tvIdDev)
        var btnEliminar = view.findViewById<ImageButton>(R.id.btnEliminar)

        fun rellenarFila(desarrollador: Desarrollador) {
            tvNombreDev.text = desarrollador.nombre
            tvIdDev.text = desarrollador.id.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_desarrollador, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = listaDesarrolladores.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = listaDesarrolladores[position]
        holder.rellenarFila(item)

        holder.btnEliminar.setOnClickListener {
            eliminarItem?.invoke(item)
        }
    }
}
