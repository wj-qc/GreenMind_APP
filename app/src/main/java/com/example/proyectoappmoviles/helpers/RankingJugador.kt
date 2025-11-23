package com.example.proyectoappmoviles.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoappmoviles.R
import com.example.proyectoappmoviles.model.RankingJugador

class RankingAdapter(private val lista: List<RankingJugador>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPosicion: TextView = view.findViewById(R.id.tvPosicion)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvPuntaje: TextView = view.findViewById(R.id.tvPuntaje)
        val tvCuestionarios: TextView = view.findViewById(R.id.tvCuestionarios)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val jugador = lista[position]
        holder.tvPosicion.text = (position + 1).toString()
        holder.tvNombre.text = jugador.nombre
        holder.tvPuntaje.text = jugador.puntaje_total.toString()
        holder.tvCuestionarios.text = jugador.cuestionariosJugados.toString()

        val prefs = holder.itemView.context.getSharedPreferences("config_juego", Context.MODE_PRIVATE)
        val tamanoTexto = prefs.getInt("tamano_texto", 16)
        val contraste = prefs.getBoolean("contraste", false)

        holder.tvPosicion.textSize = tamanoTexto.toFloat()
        holder.tvNombre.textSize = tamanoTexto.toFloat()
        holder.tvPuntaje.textSize = tamanoTexto.toFloat()
        holder.tvCuestionarios.textSize = tamanoTexto.toFloat()

        holder.itemView.alpha = if (contraste) 0.7f else 1.0f
    }

}