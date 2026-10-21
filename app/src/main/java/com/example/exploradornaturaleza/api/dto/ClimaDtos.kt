package com.example.exploradornaturaleza.api.dto

import com.google.gson.annotations.SerializedName

// GET https://api.open-meteo.com/v1/forecast?latitude=-16.5&longitude=-68.15
//     &current=temperature_2m,relative_humidity_2m,wind_speed_10m&timezone=America/La_Paz
// Los datos están dentro del objeto "current".
data class ClimaRespuestaDto(
    @SerializedName("timezone") val zonaHoraria: String?,
    @SerializedName("current_units") val unidades: ClimaUnidadesDto?,
    @SerializedName("current") val actual: ClimaActualDto?
)

data class ClimaActualDto(
    @SerializedName("time") val hora: String?,
    @SerializedName("temperature_2m") val temperatura: Double?,
    @SerializedName("relative_humidity_2m") val humedad: Int?,
    @SerializedName("wind_speed_10m") val velocidadViento: Double?
)

data class ClimaUnidadesDto(
    @SerializedName("temperature_2m") val temperatura: String?,
    @SerializedName("relative_humidity_2m") val humedad: String?,
    @SerializedName("wind_speed_10m") val velocidadViento: String?
)
