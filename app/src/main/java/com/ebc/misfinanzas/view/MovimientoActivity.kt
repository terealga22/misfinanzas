package com.ebc.misfinanzas.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ebc.misfinanzas.databinding.ActivityMovimientoBinding
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.util.NotificationHelper
import com.ebc.misfinanzas.util.RetrofitInstance
import com.ebc.misfinanzas.util.BanxicoResponse
import com.ebc.misfinanzas.viewmodel.MovimientoViewModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovimientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovimientoBinding
    private lateinit var notificationHelper: NotificationHelper
    private val viewModel: MovimientoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationHelper = NotificationHelper(this)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Cargar movimientos del usuario
        viewModel.cargarMovimientosPorUsuario(userId)

        // Mostrar lista de movimientos con signo + o -
        viewModel.movimientos.observe(this) { lista ->
            val texto = lista.joinToString("\n") {
                val simbolo = if (it.tipo.lowercase() == "ingreso") "+" else "-"
                "${it.descripcion} $simbolo$${it.cantidad}"
            }
            binding.textViewMovimientos.text = texto
        }

        // Obtener tipo de cambio desde Banxico (usando enqueue)
        val call = RetrofitInstance.api.obtenerTipoCambio()
        call.enqueue(object : Callback<BanxicoResponse> {
            override fun onResponse(
                call: Call<BanxicoResponse>,
                response: Response<BanxicoResponse>
            ) {
                if (response.isSuccessful) {
                    val tipoCambio = response.body()?.bmx?.series?.firstOrNull()?.datos?.firstOrNull()?.dato
                    binding.textViewTipoCambio.text = "Tipo de cambio: $tipoCambio MXN/USD"
                } else {
                    binding.textViewTipoCambio.text = "Error al cargar tipo de cambio"
                }
            }

            override fun onFailure(call: Call<BanxicoResponse>, t: Throwable) {
                binding.textViewTipoCambio.text = "Fallo: ${t.message}"
            }
        })

        // Guardar nuevo movimiento
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

                // Lottie animación
                binding.lottieGuardar.visibility = View.VISIBLE
                binding.lottieGuardar.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.lottieGuardar.visibility = View.GONE
                }, 2000)

                // Notificación
                notificationHelper.mostrarNotificacion(
                    titulo = "Movimiento guardado",
                    mensaje = "Se registró \"$descripcion\" por $$monto"
                )

                // Limpiar campos
                binding.editTextDescripcion.text.clear()
                binding.editTextMonto.text.clear()
            } else {
                Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
