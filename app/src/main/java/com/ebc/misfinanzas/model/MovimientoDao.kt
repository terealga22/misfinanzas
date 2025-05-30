package com.ebc.misfinanzas.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimiento WHERE userId = :userId")
    suspend fun getMovimientosPorUsuario(userId: String): List<Movimiento>


    @Insert
    suspend fun insertarMovimiento(movimiento: Movimiento)

    @Delete
    suspend fun eliminarMovimiento(movimiento: Movimiento)
}

