package com.ebc.misfinanzas.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ebc.misfinanzas.databinding.ActivityMovimientoBinding
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.util.NotificationHelper
import com.ebc.misfinanzas.viewmodel.MovimientoViewModel
import com.google.firebase.auth.FirebaseAuth

class MovimientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovimientoBinding
    private lateinit var notificationHelper: NotificationHelper

    // ViewModel directamente (usa AndroidViewModel, no necesita factory personalizada)
    private val viewModel: MovimientoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar helper para notificaciones
        notificationHelper = NotificationHelper(this)

        // Obtener el UID del usuario actual
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Cargar movimientos del usuario
        viewModel.cargarMovimientosPorUsuario(userId)

        // Observar los movimientos y mostrarlos
        viewModel.movimientos.observe(this) { lista ->
            val texto = lista.joinToString("\n") { it.descripcion + " - $" + it.cantidad }
            binding.textViewMovimientos.text = texto
        }

        // Insertar un nuevo movimiento
        binding.buttonGuardar.setOnClickListener {
            val descripcion = binding.editTextDescripcion.text.toString()
            val monto = binding.editTextMonto.text.toString().toDoubleOrNull()
            val tipoSeleccionado = binding.spinnerTipo.selectedItem.toString()

            if (descripcion.isNotBlank() && monto != null) {
                val nuevoMovimiento = Movimiento(
                    descripcion = descripcion,
                    cantidad = monto,
                    tipo = tipoSeleccionado,
                    fecha = System.currentTimeMillis().toString(),
                    userId = userId
                )
                viewModel.insertarMovimiento(nuevoMovimiento)

                // Mostrar notificación
                val mensaje = "$tipoSeleccionado: $descripcion por $$monto"
                notificationHelper.mostrarNotificacion("Nuevo movimiento", mensaje)

                // Limpiar campos
                binding.editTextDescripcion.text.clear()
                binding.editTextMonto.text.clear()
            }
        }
    }
}
