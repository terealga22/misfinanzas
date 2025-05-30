package com.ebc.misfinanzas.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimiento ORDER BY fecha DESC")
    suspend fun obtenerTodos(): List<Movimiento>

    @Insert
    suspend fun insertar(movimiento: Movimiento)
}