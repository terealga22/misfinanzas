package com.ebc.misfinanzas.viewmodel

import androidx.lifecycle.*
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.repository.MovimientoRepository
import kotlinx.coroutines.launch

class MovimientoViewModel(private val repository: MovimientoRepository) : ViewModel() {

    private val _userId = MutableLiveData<String>()

    val movimientos: LiveData<List<Movimiento>> = _userId.switchMap { userId ->
        repository.obtenerMovimientosPorUsuario(userId)
    }

    fun cargarMovimientosPorUsuario(userId: String) {
        _userId.value = userId
    }

    fun insertarMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            repository.insertar(movimiento)
            // Se vuelve a cargar al insertar
            _userId.value = movimiento.userId
        }
    }
}













