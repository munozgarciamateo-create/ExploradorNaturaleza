package com.example.exploradornaturaleza.model

// Lo que devuelve un repositorio: o salió bien (con datos) o salió mal (con un mensaje).
// Así el ViewModel nunca tiene que atrapar excepciones de red.
sealed interface Resultado<out T> {
    data class Exito<T>(val datos: T) : Resultado<T>
    data class Error(val mensaje: String) : Resultado<Nothing>
}
