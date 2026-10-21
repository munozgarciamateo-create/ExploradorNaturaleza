package com.example.exploradornaturaleza.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exploradornaturaleza.ui.componentes.MensajeVacio
import com.example.exploradornaturaleza.ui.componentes.PantallaCargando
import com.example.exploradornaturaleza.ui.componentes.PantallaError
import com.example.exploradornaturaleza.ui.componentes.TituloPantalla
import com.example.exploradornaturaleza.ui.viewmodel.GatosViewModel

// Diez datos curiosos sobre gatos (Cat Facts): campos fact y length.
@Composable
fun GatosScreen(viewModel: GatosViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TituloPantalla("Datos de gatos")

        when {
            estado.cargando -> PantallaCargando()

            estado.error != null -> PantallaError(
                mensaje = estado.error ?: "",
                onReintentar = viewModel::reintentar
            )

            estado.datos.isEmpty() -> MensajeVacio("No hay datos de gatos para mostrar.")

            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(estado.datos) { indice, dato ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Dato ${indice + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(dato.texto, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "Longitud: ${dato.longitud} caracteres",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
