package com.ebc.misfinanzas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val cantidad: Double,
    val tipo: String,
    val fecha: String,
    val userId: String  // Nuevo campo para vincular al usuario
)
