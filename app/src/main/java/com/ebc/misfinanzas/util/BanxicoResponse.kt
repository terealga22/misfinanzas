package com.ebc.misfinanzas.util

data class BanxicoResponse(
    val bmx: Bmx
)

data class Bmx(
    val series: List<Serie>
)

data class Serie(
    val idSerie: String,
    val datos: List<Dato>
)

data class Dato(
    val fecha: String,
    val dato: String
)
