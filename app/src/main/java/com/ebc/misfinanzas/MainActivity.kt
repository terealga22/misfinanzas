package com.ebc.misfinanzas

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ebc.misfinanzas.util.RetrofitInstance
import com.ebc.misfinanzas.util.BanxicoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var tvTipoCambio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTipoCambio = findViewById(R.id.tvTipoCambio)

        obtenerTipoCambio()
    }

    private fun obtenerTipoCambio() {
        val call = RetrofitInstance.api.obtenerTipoCambio()

        call.enqueue(object : Callback<BanxicoResponse> {
            override fun onResponse(call: Call<BanxicoResponse>, response: Response<BanxicoResponse>) {
                if (response.isSuccessful) {
                    val dato = response.body()?.bmx?.series?.firstOrNull()?.datos?.firstOrNull()?.dato
                    tvTipoCambio.text = "Tipo de cambio: $dato"
                } else {
                    tvTipoCambio.text = "Error al obtener datos"
                }
            }

            override fun onFailure(call: Call<BanxicoResponse>, t: Throwable) {
                tvTipoCambio.text = "Fallo: ${t.message}"
            }
        })
    }
}


