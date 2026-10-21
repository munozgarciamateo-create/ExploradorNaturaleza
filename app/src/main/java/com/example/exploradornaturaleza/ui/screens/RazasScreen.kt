package com.example.exploradornaturaleza.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exploradornaturaleza.model.Raza
import com.example.exploradornaturaleza.ui.componentes.MensajeVacio
import com.example.exploradornaturaleza.ui.componentes.PantallaCargando
import com.example.exploradornaturaleza.ui.componentes.PantallaError
import com.example.exploradornaturaleza.ui.componentes.TituloPantalla
import com.example.exploradornaturaleza.ui.viewmodel.RazasViewModel

// Pantalla principal: lista de razas (Dog CEO) con buscador.
@Composable
fun RazasScreen(
    onRazaClick: (String) -> Unit,
    viewModel: RazasViewModel = viewModel()
) {
    // Binding: la pantalla escucha el estado del ViewModel y se redibuja sola.
    val estado by viewModel.uiState.collectAsState()

    // Texto del buscador (estado propio de la UI, sobrevive a la rotación).
    var busqueda by rememberSaveable { mutableStateOf("") }

    // derivedStateOf: la lista filtrada solo se recalcula cuando cambia "busqueda"
    // o la lista de razas, no en cada recomposición.
    val razasFiltradas by remember(estado.razas) {
        derivedStateOf {
            val texto = busqueda.trim()
            if (texto.isEmpty()) estado.razas
            else estado.razas.filter { it.nombre.contains(texto, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TituloPantalla("Razas de perros")

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar raza") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(onClick = { busqueda = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        when {
            estado.cargando -> PantallaCargando()

            estado.error != null -> PantallaError(
                mensaje = estado.error ?: "",
                onReintentar = viewModel::reintentar
            )

            razasFiltradas.isEmpty() -> MensajeVacio("No hay razas que coincidan con \"$busqueda\".")

            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(razasFiltradas, key = { it.nombre }) { raza ->
                    FilaRaza(raza = raza, onClick = { onRazaClick(raza.nombre) })
                }
            }
        }
    }
}

@Composable
private fun FilaRaza(raza: Raza, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = raza.nombre.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = textoSubrazas(raza.cantidadSubrazas),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun textoSubrazas(cantidad: Int): String = when (cantidad) {
    0 -> "Sin subrazas"
    1 -> "1 subraza"
    else -> "$cantidad subrazas"
}
