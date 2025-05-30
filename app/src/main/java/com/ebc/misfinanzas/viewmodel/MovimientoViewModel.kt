package com.ebc.misfinanzas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.repository.MovimientoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class MovimientoViewModel(private val repository: MovimientoRepository) : ViewModel() {

    private val _movimientos = MutableLiveData<List<Movimiento>>()
    val movimientos: LiveData<List<Movimiento>> = _movimientos

    fun cargarMovimientos() {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerMovimientos()
            _movimientos.postValue(lista)
        }
    }

    fun insertarMovimiento(movimiento: Movimiento) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertarMovimiento(movimiento)
            cargarMovimientos() // Actualizar lista después de insertar
        }
    }
}

