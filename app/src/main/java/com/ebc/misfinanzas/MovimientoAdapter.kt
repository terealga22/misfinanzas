package com.ebc.misfinanzas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebc.misfinanzas.databinding.ItemMovimientoBinding
import com.ebc.misfinanzas.model.Movimiento

class MovimientoAdapter : ListAdapter<Movimiento, MovimientoAdapter.MovimientoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val binding = ItemMovimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = getItem(position)
        holder.bind(movimiento)
    }

    class MovimientoViewHolder(private val binding: ItemMovimientoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mov: Movimiento) {
            binding.tvDescripcion.text = mov.descripcion
            binding.tvCantidad.text = "$${mov.cantidad}"
            binding.tvTipo.text = mov.tipo
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movimiento>() {
        override fun areItemsTheSame(oldItem: Movimiento, newItem: Movimiento): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movimiento, newItem: Movimiento): Boolean = oldItem == newItem
    }
}
