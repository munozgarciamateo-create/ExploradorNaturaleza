package com.example.exploradornaturaleza.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.model.Resultado
import com.example.exploradornaturaleza.repository.RazaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Todo lo que la pantalla necesita saber, en un solo objeto inmutable.
data class RazasUiState(
    val cargando: Boolean = false,
    val razas: List<Raza> = emptyList(),
    val error: String? = null
)

class RazasViewModel(
    private val repositorio: RazaRepository = RazaRepository()
) : ViewModel() {

    // Privado y mutable: solo el ViewModel cambia el estado.
    private val _uiState = MutableStateFlow(RazasUiState(cargando = true))
    // Público y de solo lectura: la View lo observa.
    val uiState: StateFlow<RazasUiState> = _uiState.asStateFlow()

    private var trabajo: Job? = null

    init {
        cargar()
    }

    fun reintentar() = cargar()

    private fun cargar() {
        trabajo?.cancel() // si había una carga en curso, se cancela para no pisar el resultado
        _uiState.update { it.copy(cargando = true, error = null) }
        trabajo = viewModelScope.launch {
            when (val resultado = repositorio.obtenerRazas()) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, razas = resultado.datos, error = null)
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}
