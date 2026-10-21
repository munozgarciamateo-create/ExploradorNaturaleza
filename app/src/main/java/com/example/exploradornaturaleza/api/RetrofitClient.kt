package com.example.exploradornaturaleza.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Una instancia de Retrofit POR CADA baseUrl (cada API vive en un dominio distinto).
// "by lazy" = se crea la primera vez que se usa y después se reutiliza.
object RetrofitClient {
    private const val URL_DOG = "https://dog.ceo/api/"
    private const val URL_CLIMA = "https://api.open-meteo.com/v1/"
    private const val URL_GATOS = "https://catfact.ninja/"

    // Un solo cliente HTTP compartido (reutiliza conexiones) con tiempos límite.
    private val cliente: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun crearRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create()) // JSON -> DTO
            .build()

    val dogApi: DogApiService by lazy {
        crearRetrofit(URL_DOG).create(DogApiService::class.java)
    }

    val climaApi: ClimaApiService by lazy {
        crearRetrofit(URL_CLIMA).create(ClimaApiService::class.java)
    }

    val gatoApi: GatoApiService by lazy {
        crearRetrofit(URL_GATOS).create(GatoApiService::class.java)
    }
}
