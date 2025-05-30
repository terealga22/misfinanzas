package com.ebc.misfinanzas.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: BanxicoApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.banxico.org.mx/SieAPIRest/service/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BanxicoApiService::class.java)
    }
}

