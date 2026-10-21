package com.example.exploradornaturaleza.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exploradornaturaleza.model.RazaFavorita
import com.example.exploradornaturaleza.ui.componentes.MensajeVacio
import com.example.exploradornaturaleza.ui.componentes.PantallaCargando
import com.example.exploradornaturaleza.ui.componentes.TituloPantalla
import com.example.exploradornaturaleza.ui.viewmodel.FavoritasViewModel

// Razas guardadas en Room. Persisten aunque se cierre la app.
@Composable
fun FavoritasScreen(
    onRazaClick: (String) -> Unit,
    viewModel: FavoritasViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TituloPantalla("Razas favoritas")

        when {
            estado.cargando -> PantallaCargando()

            estado.favoritas.isEmpty() -> MensajeVacio("Sin elementos guardados")

            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(estado.favoritas, key = { it.nombre }) { favorita ->
                    FilaFavorita(
                        favorita = favorita,
                        onClick = { onRazaClick(favorita.nombre) },
                        onQuitar = { viewModel.quitar(favorita.nombre) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaFavorita(
    favorita: RazaFavorita,
    onClick: () -> Unit,
    onQuitar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorita.nombre.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = textoSubrazas(favorita.cantidadSubrazas),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onQuitar) {
                Icon(Icons.Default.Delete, contentDescription = "Quitar ${favorita.nombre} de favoritas")
            }
        }
    }
}
