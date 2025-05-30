package com.ebc.misfinanzas.repository

import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.model.MovimientoDao

class MovimientoRepository(private val movimientoDao: MovimientoDao) {

    suspend fun obtenerMovimientos(): List<Movimiento> {
        return movimientoDao.obtenerTodos()
    }

    suspend fun insertarMovimiento(movimiento: Movimiento) {
        movimientoDao.insertar(movimiento)
    }
}

