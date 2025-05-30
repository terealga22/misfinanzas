package com.ebc.misfinanzas.util

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.Call

interface BanxicoApiService {
    @Headers(
        "Accept: application/json",
        "Bmx-Token: 3b24593f6ee6c47cc4e0913b1003c06f8305ed034d7ef0b26a7e500d1035ea8d"
    )
    @GET("series/SF43718/datos/oportuno")
    fun obtenerTipoCambio(): Call<BanxicoResponse>
}
