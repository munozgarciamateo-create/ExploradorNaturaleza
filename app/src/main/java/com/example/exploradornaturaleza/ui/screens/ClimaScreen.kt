package com.example.exploradornaturaleza.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exploradornaturaleza.ui.componentes.PantallaCargando
import com.example.exploradornaturaleza.ui.componentes.PantallaError
import com.example.exploradornaturaleza.ui.componentes.TituloPantalla
import com.example.exploradornaturaleza.ui.viewmodel.ClimaViewModel

// Clima actual en La Paz (Open-Meteo): temperatura, humedad y viento.
@Composable
fun ClimaScreen(viewModel: ClimaViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TituloPantalla("Clima en La Paz")

        val clima = estado.clima
        when {
            estado.cargando -> PantallaCargando()

            estado.error != null -> PantallaError(
                mensaje = estado.error ?: "",
                onReintentar = viewModel::reintentar
            )

            clima != null -> Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DatoClima("Temperatura", "${clima.temperatura} ${clima.unidadTemperatura}")
                DatoClima("Humedad", "${clima.humedad} ${clima.unidadHumedad}")
                DatoClima("Velocidad del viento", "${clima.velocidadViento} ${clima.unidadViento}")
                Text(
                    text = "Actualizado: ${clima.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedButton(onClick = viewModel::reintentar) {
                    Text("Actualizar")
                }
            }
        }
    }
}

@Composable
private fun DatoClima(etiqueta: String, valor: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(etiqueta, style = MaterialTheme.typography.labelLarge)
            Text(valor, style = MaterialTheme.typography.headlineMedium)
        }
    }
}
