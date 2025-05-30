package com.ebc.misfinanzas.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ebc.misfinanzas.model.AppDatabase
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.repository.MovimientoRepository
import kotlinx.coroutines.launch

class MovimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovimientoRepository

    private val _movimientos = MutableLiveData<List<Movimiento>>()
    val movimientos: LiveData<List<Movimiento>> get() = _movimientos

    init {
        // Aquí sí puedes usar 'application' porque viene en el constructor
        val dao = AppDatabase.getDatabase(application).movimientoDao()
        repository = MovimientoRepository(dao)
    }

    fun cargarMovimientosPorUsuario(userId: String) {
        viewModelScope.launch {
            val lista = repository.obtenerMovimientosPorUsuario(userId)
            _movimientos.postValue(lista)
        }
    }

    fun insertarMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            repository.insertar(movimiento)
            // Recarga los movimientos después de insertar
            cargarMovimientosPorUsuario(movimiento.userId)
        }
    }
}


