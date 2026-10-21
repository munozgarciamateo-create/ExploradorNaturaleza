package com.example.exploradornaturaleza.model

// Modelo del dominio: lo que usa la app (no depende del formato de las APIs ni de Room).

data class Raza(
    val nombre: String,
    val subrazas: List<String>
) {
    val cantidadSubrazas: Int get() = subrazas.size
}

data class Clima(
    val temperatura: Double,
    val unidadTemperatura: String,
    val humedad: Int,
    val unidadHumedad: String,
    val velocidadViento: Double,
    val unidadViento: String,
    val hora: String
)

data class DatoGato(
    val texto: String,
    val longitud: Int
)

data class RazaFavorita(
    val nombre: String,
    val cantidadSubrazas: Int
)
