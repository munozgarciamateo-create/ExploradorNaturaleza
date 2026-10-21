package com.example.exploradornaturaleza.mapper

import com.example.exploradornaturaleza.api.dto.ClimaRespuestaDto
import com.example.exploradornaturaleza.api.dto.GatosRespuestaDto
import com.example.exploradornaturaleza.api.dto.RazasRespuestaDto
import com.example.exploradornaturaleza.api.dto.SubrazasRespuestaDto
import com.example.exploradornaturaleza.model.Clima
import com.example.exploradornaturaleza.model.DatoGato
import com.example.exploradornaturaleza.model.Raza

// La API respondió, pero le falta algo obligatorio.
class DatosInvalidosException(mensaje: String) : Exception(mensaje)

// Dog CEO: el MAPA {"bulldog": ["boston", ...]} se convierte en una LISTA de Raza.
fun RazasRespuestaDto.aRazas(): List<Raza> =
    razas.orEmpty()
        .map { (nombre, subrazas) -> Raza(nombre = nombre, subrazas = subrazas.orEmpty()) }
        .sortedBy { it.nombre }

fun SubrazasRespuestaDto.aRaza(nombre: String): Raza =
    Raza(nombre = nombre, subrazas = subrazas.orEmpty().sorted())

// Open-Meteo: los datos vienen dentro de "current".
fun ClimaRespuestaDto.aClima(): Clima {
    val datos = actual ?: throw DatosInvalidosException("La respuesta del clima no trae el objeto \"current\".")
    return Clima(
        temperatura = datos.temperatura ?: throw DatosInvalidosException("Falta la temperatura."),
        unidadTemperatura = unidades?.temperatura ?: "°C",
        humedad = datos.humedad ?: throw DatosInvalidosException("Falta la humedad."),
        unidadHumedad = unidades?.humedad ?: "%",
        velocidadViento = datos.velocidadViento ?: throw DatosInvalidosException("Falta la velocidad del viento."),
        unidadViento = unidades?.velocidadViento ?: "km/h",
        hora = datos.hora?.replace("T", " ") ?: "-"
    )
}

// Cat Facts: la lista viene dentro de "data". Se descartan los datos sin texto.
fun GatosRespuestaDto.aDatosGato(): List<DatoGato> =
    datos.orEmpty().mapNotNull { dto ->
        val texto = dto.dato ?: return@mapNotNull null
        DatoGato(texto = texto, longitud = dto.longitud ?: texto.length)
    }
