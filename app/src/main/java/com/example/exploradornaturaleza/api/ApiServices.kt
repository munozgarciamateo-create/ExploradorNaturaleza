package com.example.exploradornaturaleza.api

import com.example.exploradornaturaleza.api.dto.ClimaRespuestaDto
import com.example.exploradornaturaleza.api.dto.GatosRespuestaDto
import com.example.exploradornaturaleza.api.dto.RazasRespuestaDto
import com.example.exploradornaturaleza.api.dto.SubrazasRespuestaDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// baseUrl: https://dog.ceo/api/
interface DogApiService {
    // GET https://dog.ceo/api/breeds/list/all
    @GET("breeds/list/all")
    suspend fun listarRazas(): RazasRespuestaDto

    // GET https://dog.ceo/api/breed/bulldog/list  (@Path mete el nombre dentro de la ruta)
    @GET("breed/{nombre}/list")
    suspend fun listarSubrazas(@Path("nombre") nombre: String): SubrazasRespuestaDto
}

// baseUrl: https://api.open-meteo.com/v1/
interface ClimaApiService {
    // GET .../forecast?latitude=-16.5&longitude=-68.15&current=...&timezone=America/La_Paz
    // (@Query agrega cada parámetro después del "?")
    @GET("forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitud: Double = -16.5,
        @Query("longitude") longitud: Double = -68.15,
        @Query("current") variables: String = "temperature_2m,relative_humidity_2m,wind_speed_10m",
        @Query("timezone") zonaHoraria: String = "America/La_Paz"
    ): ClimaRespuestaDto
}

// baseUrl: https://catfact.ninja/
interface GatoApiService {
    // GET https://catfact.ninja/facts?limit=10
    @GET("facts")
    suspend fun obtenerDatos(@Query("limit") limite: Int = 10): GatosRespuestaDto
}
