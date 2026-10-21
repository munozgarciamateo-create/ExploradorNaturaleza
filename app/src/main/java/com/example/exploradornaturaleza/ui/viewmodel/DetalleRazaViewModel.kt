package com.example.exploradornaturaleza.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.exploradornaturaleza.db.AppDatabase
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.model.Resultado
import com.example.exploradornaturaleza.repository.FavoritasRepository
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
    val error: String? = null,
    val esFavorita: Boolean = false
)

// SavedStateHandle trae el argumento de navegación "nombre" (ruta "detalle/{nombre}").
// AndroidViewModel da el contexto de la Application (seguro: no muere al girar la pantalla).
class DetalleRazaViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val nombre: String = savedStateHandle.get<String>(Rutas.ARG_NOMBRE).orEmpty()

    private val razaRepository = RazaRepository()
    private val favoritasRepository =
        FavoritasRepository(AppDatabase.obtener(application).razaFavoritaDao())

    private val _uiState = MutableStateFlow(DetalleRazaUiState(cargando = true))
    val uiState: StateFlow<DetalleRazaUiState> = _uiState.asStateFlow()

    private var trabajo: Job? = null

    init {
        cargar()
        // Escucha en vivo si esta raza está guardada en Room.
        viewModelScope.launch {
            favoritasRepository.esFavorita(nombre).collect { favorita ->
                _uiState.update { it.copy(esFavorita = favorita) }
            }
        }
    }

    fun reintentar() = cargar()

    // Botón "Marcar favorita": guarda desde el ViewModel (la View solo avisa).
    fun marcarFavorita() {
        val raza = _uiState.value.raza ?: return
        viewModelScope.launch { favoritasRepository.guardar(raza) }
    }

    fun quitarFavorita() {
        viewModelScope.launch { favoritasRepository.quitar(nombre) }
    }

    private fun cargar() {
        trabajo?.cancel()
        _uiState.update { it.copy(cargando = true, error = null) }
        trabajo = viewModelScope.launch {
            when (val resultado = razaRepository.obtenerRaza(nombre)) {
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
