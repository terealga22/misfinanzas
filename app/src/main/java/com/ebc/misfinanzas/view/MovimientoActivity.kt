package com.ebc.misfinanzas.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ebc.misfinanzas.databinding.ActivityMovimientoBinding
import com.ebc.misfinanzas.model.AppDatabase
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.repository.MovimientoRepository
import com.ebc.misfinanzas.viewmodel.MovimientoViewModel

class MovimientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovimientoBinding

    // ViewModel con Factory para pasar el repositorio
    private val viewModel: MovimientoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = AppDatabase.getDatabase(application).movimientoDao()
                val repository = MovimientoRepository(dao)
                return MovimientoViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar movimientos desde Room
        viewModel.cargarMovimientos()

        // Observar cambios en los datos
        viewModel.movimientos.observe(this) { lista ->
            val texto = lista.joinToString("\n") { it.descripcion + " - $" + it.cantidad }
            binding.textViewMovimientos.text = texto
        }

        // Insertar nuevo movimiento
        binding.buttonGuardar.setOnClickListener {
            val descripcion = binding.editTextDescripcion.text.toString()
            val monto = binding.editTextMonto.text.toString().toDoubleOrNull()
            val tipoSeleccionado = binding.spinnerTipo.selectedItem.toString()

            if (descripcion.isNotBlank() && monto != null) {
                val nuevoMovimiento = Movimiento(
                    descripcion = descripcion,
                    cantidad = monto,
                    tipo = tipoSeleccionado,
                    fecha = System.currentTimeMillis().toString()
                )
                viewModel.insertarMovimiento(nuevoMovimiento)

                binding.editTextDescripcion.text.clear()
                binding.editTextMonto.text.clear()
            }
        }
    }
}


