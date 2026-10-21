package com.example.exploradornaturaleza.repository

import com.example.exploradornaturaleza.api.ClimaApiService
import com.example.exploradornaturaleza.api.DogApiService
import com.example.exploradornaturaleza.api.GatoApiService
import com.example.exploradornaturaleza.api.RetrofitClient
import com.example.exploradornaturaleza.mapper.aClima
import com.example.exploradornaturaleza.mapper.aDatosGato
import com.example.exploradornaturaleza.mapper.aRaza
import com.example.exploradornaturaleza.mapper.aRazas
import com.example.exploradornaturaleza.model.Clima
import com.example.exploradornaturaleza.model.DatoGato
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.model.Resultado

// Los repositorios reciben el servicio por constructor (se puede cambiar por uno falso en pruebas)
// y siempre devuelven Resultado: nunca lanzan excepciones de red hacia el ViewModel.

class RazaRepository(private val api: DogApiService = RetrofitClient.dogApi) {

    suspend fun obtenerRazas(): Resultado<List<Raza>> =
        llamadaSegura { api.listarRazas().aRazas() }

    suspend fun obtenerRaza(nombre: String): Resultado<Raza> =
        llamadaSegura { api.listarSubrazas(nombre).aRaza(nombre) }
}

class ClimaRepository(private val api: ClimaApiService = RetrofitClient.climaApi) {

    suspend fun obtenerClimaLaPaz(): Resultado<Clima> =
        llamadaSegura { api.obtenerClima().aClima() }
}

class GatoRepository(private val api: GatoApiService = RetrofitClient.gatoApi) {

    suspend fun obtenerDatos(): Resultado<List<DatoGato>> =
        llamadaSegura { api.obtenerDatos(limite = 10).aDatosGato() }
}
