package com.ebc.misfinanzas.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ebc.misfinanzas.R
import com.ebc.misfinanzas.model.Movimiento

class MovimientoAdapter(
    private val movimientos: List<Movimiento>,
    private val onEditClick: (Movimiento) -> Unit,
    private val onDeleteClick: (Movimiento) -> Unit
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    inner class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val textMonto: TextView = itemView.findViewById(R.id.textViewMonto)
        val textFecha: TextView = itemView.findViewById(R.id.textViewFecha)
        val editBtn: ImageView = itemView.findViewById(R.id.imageViewEdit)
        val deleteBtn: ImageView = itemView.findViewById(R.id.imageViewDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = movimientos[position]
        holder.textNombre.text = movimiento.descripcion
        holder.textMonto.text = "$${movimiento.cantidad}"
        holder.textFecha.text = movimiento.fecha

        holder.editBtn.setOnClickListener { onEditClick(movimiento) }
        holder.deleteBtn.setOnClickListener { onDeleteClick(movimiento) }
    }

    override fun getItemCount(): Int = movimientos.size
}


