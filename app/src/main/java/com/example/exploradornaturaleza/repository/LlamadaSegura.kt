package com.example.exploradornaturaleza.repository

import com.example.exploradornaturaleza.mapper.DatosInvalidosException
import com.example.exploradornaturaleza.model.Resultado
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

// Ejecuta una llamada a la API y convierte cualquier error en Resultado.Error con un mensaje claro.
// El orden de los catch importa: del más específico al más general.
suspend fun <T> llamadaSegura(bloque: suspend () -> T): Resultado<T> =
    try {
        Resultado.Exito(bloque())
    } catch (e: CancellationException) {
        throw e // cancelar una corrutina NO es un error: se deja pasar
    } catch (e: HttpException) {
        Resultado.Error(
            when (e.code()) {
                404 -> "No se encontró la información solicitada (404)."
                in 500..599 -> "El servidor tuvo un problema (HTTP ${e.code()}). Intenta nuevamente."
                else -> "El servicio respondió con un error (HTTP ${e.code()})."
            }
        )
    } catch (e: SocketTimeoutException) {
        // Va ANTES de IOException porque SocketTimeoutException es hija de IOException.
        Resultado.Error("La consulta tardó demasiado. Intenta nuevamente.")
    } catch (e: IOException) {
        Resultado.Error("No se pudo conectar. Revisa tu conexión a Internet.")
    } catch (e: JsonParseException) {
        Resultado.Error("El servicio devolvió datos que no se pudieron leer.")
    } catch (e: DatosInvalidosException) {
        Resultado.Error(e.message ?: "Los datos recibidos están incompletos.")
    }
