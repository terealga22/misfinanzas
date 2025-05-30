package com.ebc.misfinanzas.util

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.Call

interface BanxicoApiService {
    @Headers(
        "Bmx-Token: 1b254564f3765c4bcd30f7bd0cc980aefbb372263236a69305ea71f48526a4c8" // Reemplaza por tu token real
    )
    @GET("SieAPIRest/service/v1/series/SF43718/datos/oportuno")
    fun obtenerTipoCambio(): Call<BanxicoResponse>
}


