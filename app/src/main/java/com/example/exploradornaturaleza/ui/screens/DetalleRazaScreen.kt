package com.example.exploradornaturaleza.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.exploradornaturaleza.ui.componentes.MensajeVacio
import com.example.exploradornaturaleza.ui.componentes.PantallaCargando
import com.example.exploradornaturaleza.ui.componentes.PantallaError
import com.example.exploradornaturaleza.ui.viewmodel.DetalleRazaViewModel

// Detalle: nombre de la raza (llega como argumento de navegación) y sus subrazas.
@Composable
fun DetalleRazaScreen(
    onVolver: () -> Unit,
    viewModel: DetalleRazaViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = viewModel.nombre.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineSmall
            )
        }

        val raza = estado.raza
        when {
            estado.cargando -> PantallaCargando()

            estado.error != null -> PantallaError(
                mensaje = estado.error ?: "",
                onReintentar = viewModel::reintentar
            )

            raza != null -> Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = textoSubrazas(raza.cantidadSubrazas),
                    style = MaterialTheme.typography.titleMedium
                )

                if (raza.subrazas.isEmpty()) {
                    MensajeVacio("Esta raza no tiene subrazas.")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(raza.subrazas, key = { it }) { subraza ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "${subraza.replaceFirstChar { it.uppercase() }} ${raza.nombre}",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
