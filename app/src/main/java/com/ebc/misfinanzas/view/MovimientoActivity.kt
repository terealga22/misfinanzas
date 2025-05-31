package com.ebc.misfinanzas.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ebc.misfinanzas.databinding.ActivityMovimientoBinding
import com.ebc.misfinanzas.model.AppDatabase
import com.ebc.misfinanzas.model.Movimiento
import com.ebc.misfinanzas.repository.MovimientoRepository
import com.ebc.misfinanzas.util.NotificationHelper
import com.ebc.misfinanzas.util.RetrofitInstance
import com.ebc.misfinanzas.util.BanxicoResponse
import com.ebc.misfinanzas.viewmodel.MovimientoViewModel
import com.ebc.misfinanzas.viewmodel.MovimientoViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MovimientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovimientoBinding
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var viewModel: MovimientoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = MovimientoRepository(database.movimientoDao())
        val factory = MovimientoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MovimientoViewModel::class.java)

        notificationHelper = NotificationHelper(this)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        viewModel.cargarMovimientosPorUsuario(userId)

        viewModel.movimientos.observe(this) { lista ->
            val ingresos = lista.filter { it.tipo.trim().lowercase() == "ingreso" }.sumOf { it.cantidad }
            val egresos = lista.filter { it.tipo.trim().lowercase() == "egreso" }.sumOf { it.cantidad }

            val texto = lista.joinToString("\n") {
                val simbolo = if (it.tipo.trim().lowercase() == "ingreso") "+" else "-"
                "${it.descripcion} $simbolo$${it.cantidad}"
            }
            binding.textViewMovimientos.text = texto

            val textoResumen = "Ingresos: $ingresos\nEgresos: $egresos"
            binding.textViewResumen.text = textoResumen

            val pieChart = binding.pieChart
            if (ingresos == 0.0 && egresos == 0.0) {
                pieChart.clear()
                pieChart.setNoDataText("Sin datos de movimientos")
                pieChart.invalidate()
            } else {
                val entries = mutableListOf<PieEntry>()
                if (ingresos > 0) entries.add(PieEntry(ingresos.toFloat(), "Ingresos"))
                if (egresos > 0) entries.add(PieEntry(egresos.toFloat(), "Egresos"))

                val dataSet = PieDataSet(entries, "")
                dataSet.colors = listOf(Color.GREEN, Color.RED)
                dataSet.valueTextSize = 14f

                val pieData = PieData(dataSet)
                pieChart.data = pieData
                pieChart.centerText = "Finanzas"
                pieChart.setCenterTextSize(18f)
                pieChart.setUsePercentValues(true)
                pieChart.description = Description().apply { text = "" }
                pieChart.invalidate()
            }
        }



        // Tipo de cambio Banxico
        RetrofitInstance.api.obtenerTipoCambio().enqueue(object : Callback<BanxicoResponse> {
            override fun onResponse(call: Call<BanxicoResponse>, response: Response<BanxicoResponse>) {
                val tipoCambio = response.body()?.bmx?.series?.firstOrNull()?.datos?.firstOrNull()?.dato
                binding.textViewTipoCambio.text = "Tipo de cambio: $tipoCambio MXN/USD"
            }

            override fun onFailure(call: Call<BanxicoResponse>, t: Throwable) {
                binding.textViewTipoCambio.text = "Fallo: ${t.message}"
            }
        })


        ///
        binding.buttonGuardar.setOnClickListener {
            val descripcion = binding.editTextDescripcion.text.toString()
            val monto = binding.editTextMonto.text.toString().toDoubleOrNull()
            val tipoSeleccionado = binding.spinnerTipo.selectedItem.toString()

            if (descripcion.isNotBlank() && monto != null) {
                val nuevoMovimiento = Movimiento(
                    descripcion = descripcion,
                    cantidad = monto,
                    tipo = tipoSeleccionado.lowercase(),
                    fecha = System.currentTimeMillis().toString(),
                    userId = userId
                )
                viewModel.insertarMovimiento(nuevoMovimiento)

                binding.lottieGuardar.visibility = View.VISIBLE
                binding.lottieGuardar.playAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.lottieGuardar.visibility = View.GONE
                }, 1500)

                notificationHelper.mostrarNotificacion(
                    titulo = "Movimiento guardado",
                    mensaje = "Se registró \"$descripcion\" por $$monto"
                )

                binding.editTextDescripcion.text.clear()
                binding.editTextMonto.text.clear()
            } else {
                Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

