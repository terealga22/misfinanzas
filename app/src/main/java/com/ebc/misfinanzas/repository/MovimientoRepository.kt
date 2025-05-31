package com.ebc.misfinanzas.repository

import androidx.lifecycle.LiveData
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.model.MovimientoDao

class MovimientoRepository(private val dao: MovimientoDao) {
    fun obtenerMovimientosPorUsuario(userId: String): LiveData<List<Movimiento>> {
        return dao.getMovimientosByUserId(userId)
    }

    suspend fun insertar(movimiento: Movimiento) {
        dao.insertarMovimiento(movimiento)
    }

    suspend fun eliminar(movimiento: Movimiento) {
        dao.eliminarMovimiento(movimiento)
    }
}







