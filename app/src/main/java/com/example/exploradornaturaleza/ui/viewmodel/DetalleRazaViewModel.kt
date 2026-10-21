package com.example.exploradornaturaleza.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.model.Resultado
import com.example.exploradornaturaleza.repository.RazaRepository
import com.example.exploradornaturaleza.ui.navegacion.Rutas
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleRazaUiState(
    val cargando: Boolean = false,
    val raza: Raza? = null,
    val error: String? = null
)

// SavedStateHandle trae el argumento de navegación "nombre" (ruta "detalle/{nombre}").
class DetalleRazaViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val nombre: String = savedStateHandle.get<String>(Rutas.ARG_NOMBRE).orEmpty()

    private val repositorio = RazaRepository()

    private val _uiState = MutableStateFlow(DetalleRazaUiState(cargando = true))
    val uiState: StateFlow<DetalleRazaUiState> = _uiState.asStateFlow()

    private var trabajo: Job? = null

    init {
        cargar()
    }

    fun reintentar() = cargar()

    private fun cargar() {
        trabajo?.cancel()
        _uiState.update { it.copy(cargando = true, error = null) }
        trabajo = viewModelScope.launch {
            when (val resultado = repositorio.obtenerRaza(nombre)) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, raza = resultado.datos, error = null)
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
