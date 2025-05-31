package com.ebc.misfinanzas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ebc.misfinanzas.repository.MovimientoRepository

class MovimientoViewModelFactory(private val repository: MovimientoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovimientoViewModel::class.java)) {
            return MovimientoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
