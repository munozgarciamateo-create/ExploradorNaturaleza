package com.example.exploradornaturaleza.api.dto

import com.google.gson.annotations.SerializedName

// GET https://dog.ceo/api/breeds/list/all
// {"message": {"bulldog": ["boston", "english", "french"], "akita": []}, "status": "success"}
// OJO: "message" es un MAPA (raza -> lista de subrazas), no un arreglo.
data class RazasRespuestaDto(
    @SerializedName("message") val razas: Map<String, List<String>>?,
    @SerializedName("status") val estado: String?
)

// GET https://dog.ceo/api/breed/{raza}/list  (detalle de una raza)
// {"message": ["boston", "english", "french"], "status": "success"}
data class SubrazasRespuestaDto(
    @SerializedName("message") val subrazas: List<String>?,
    @SerializedName("status") val estado: String?
)
