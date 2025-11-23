package com.example.proyectoappmoviles.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoappmoviles.R
import com.example.proyectoappmoviles.model.Pregunta

class EliminarPreguntasAdapter(
    private val lista: List<Pregunta>,
    private val onClick: (Pregunta) -> Unit
) : RecyclerView.Adapter<EliminarPreguntasAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvPreguntaTitulo: TextView = view.findViewById(R.id.tvPreguntaTitulo)
        val tvOpcionesResumen: TextView = view.findViewById(R.id.tvOpcionesResumen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_pregunta_simple, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = lista[position]
        holder.tvPreguntaTitulo.text = p.pregunta
        holder.tvOpcionesResumen.text = "1) ${p.opcion1}   2) ${p.opcion2}   3) ${p.opcion3}"
        holder.itemView.setOnClickListener { onClick(p) }
    }
}
