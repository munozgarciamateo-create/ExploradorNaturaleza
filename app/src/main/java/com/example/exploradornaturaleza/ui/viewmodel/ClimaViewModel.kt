package com.example.exploradornaturaleza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exploradornaturaleza.model.Clima
import com.example.exploradornaturaleza.model.Resultado
import com.example.exploradornaturaleza.repository.ClimaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClimaUiState(
    val cargando: Boolean = false,
    val clima: Clima? = null,
    val error: String? = null
)

class ClimaViewModel(
    private val repositorio: ClimaRepository = ClimaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClimaUiState(cargando = true))
    val uiState: StateFlow<ClimaUiState> = _uiState.asStateFlow()

    private var trabajo: Job? = null

    init {
        cargar()
    }

    fun reintentar() = cargar()

    private fun cargar() {
        trabajo?.cancel()
        _uiState.update { it.copy(cargando = true, error = null) }
        trabajo = viewModelScope.launch {
            when (val resultado = repositorio.obtenerClimaLaPaz()) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, clima = resultado.datos, error = null)
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
