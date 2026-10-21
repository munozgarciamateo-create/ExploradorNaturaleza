package com.example.exploradornaturaleza.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.exploradornaturaleza.db.AppDatabase
import com.example.exploradornaturaleza.model.RazaFavorita
import com.example.exploradornaturaleza.repository.FavoritasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritasUiState(
    val cargando: Boolean = true,
    val favoritas: List<RazaFavorita> = emptyList()
)

class FavoritasViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio =
        FavoritasRepository(AppDatabase.obtener(application).razaFavoritaDao())

    // Todo sale de Room (no hay red): funciona sin internet y sobrevive al cerrar la app.
    val uiState: StateFlow<FavoritasUiState> = repositorio.observarFavoritas()
        .map { lista -> FavoritasUiState(cargando = false, favoritas = lista) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritasUiState()
        )

    // Botón quitar: Room avisa al Flow y la lista se actualiza sola.
    fun quitar(nombre: String) {
        viewModelScope.launch { repositorio.quitar(nombre) }
    }
}
