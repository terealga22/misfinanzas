package com.ebc.misfinanzas.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MovimientoDao {
    @Insert
    suspend fun insertarMovimiento(movimiento: Movimiento)

    @Delete
    suspend fun eliminarMovimiento(movimiento: Movimiento)

    @Query("SELECT * FROM movimiento WHERE userId = :userId")
    fun getMovimientosByUserId(userId: String): LiveData<List<Movimiento>>

    // 👇 Agrega aquí esta función para depurar
    @Query("SELECT * FROM movimiento")
    fun obtenerTodos(): LiveData<List<Movimiento>>
}



