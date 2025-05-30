package com.ebc.misfinanzas.repository

import androidx.lifecycle.LiveData
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.model.MovimientoDao

class MovimientoRepository(private val movimientoDao: MovimientoDao) {

    suspend fun obtenerMovimientosPorUsuario(userId: String): List<Movimiento> {
        return movimientoDao.getMovimientosPorUsuario(userId)  // O el nombre que tengas en el DAO
    }


    suspend fun insertar(movimiento: Movimiento) {
        movimientoDao.insertarMovimiento(movimiento)
    }

    suspend fun eliminar(movimiento: Movimiento) {
        movimientoDao.eliminarMovimiento(movimiento)
    }

}


