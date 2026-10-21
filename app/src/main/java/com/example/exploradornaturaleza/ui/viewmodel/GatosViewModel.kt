package com.example.exploradornaturaleza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exploradornaturaleza.model.DatoGato
import com.example.exploradornaturaleza.model.Resultado
import com.example.exploradornaturaleza.repository.GatoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GatosUiState(
    val cargando: Boolean = false,
    val datos: List<DatoGato> = emptyList(),
    val error: String? = null
)

class GatosViewModel(
    private val repositorio: GatoRepository = GatoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GatosUiState(cargando = true))
    val uiState: StateFlow<GatosUiState> = _uiState.asStateFlow()

    private var trabajo: Job? = null

    init {
        cargar()
    }

    fun reintentar() = cargar()

    private fun cargar() {
        trabajo?.cancel()
        _uiState.update { it.copy(cargando = true, error = null) }
        trabajo = viewModelScope.launch {
            when (val resultado = repositorio.obtenerDatos()) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, datos = resultado.datos, error = null)
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
