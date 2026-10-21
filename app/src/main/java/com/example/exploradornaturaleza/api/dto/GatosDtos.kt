package com.example.exploradornaturaleza.api.dto

import com.google.gson.annotations.SerializedName

// GET https://catfact.ninja/facts?limit=10
// La lista llega dentro de la propiedad "data".
data class GatosRespuestaDto(
    @SerializedName("data") val datos: List<DatoGatoDto>?
)

data class DatoGatoDto(
    @SerializedName("fact") val dato: String?,
    @SerializedName("length") val longitud: Int?
)
