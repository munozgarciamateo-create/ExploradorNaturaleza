package com.example.exploradornaturaleza.ui.navegacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exploradornaturaleza.ui.screens.ClimaScreen
import com.example.exploradornaturaleza.ui.screens.DetalleRazaScreen
import com.example.exploradornaturaleza.ui.screens.FavoritasScreen
import com.example.exploradornaturaleza.ui.screens.GatosScreen
import com.example.exploradornaturaleza.ui.screens.RazasScreen

object Rutas {
    const val RAZAS = "razas"
    const val ARG_NOMBRE = "nombre"
    const val DETALLE = "detalle/{$ARG_NOMBRE}"
    const val CLIMA = "clima"
    const val GATOS = "gatos"
    const val FAVORITAS = "favoritas"

    // El nombre de la raza viaja como argumento de navegación: "detalle/bulldog"
    fun detalle(nombre: String) = "detalle/$nombre"
}

private data class Destino(val ruta: String, val titulo: String, val icono: ImageVector)

private val destinosInferiores = listOf(
    Destino(Rutas.RAZAS, "Razas", Icons.Default.Home),
    Destino(Rutas.CLIMA, "Clima", Icons.Default.Place),
    Destino(Rutas.GATOS, "Gatos", Icons.Default.Face),
    Destino(Rutas.FAVORITAS, "Favoritas", Icons.Default.Favorite)
)

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinosInferiores.forEach { destino ->
                    NavigationBarItem(
                        selected = rutaActual == destino.ruta,
                        onClick = { navegarAPestana(navController, destino.ruta) },
                        icon = { Icon(destino.icono, contentDescription = destino.titulo) },
                        label = { Text(destino.titulo) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.RAZAS,
            modifier = Modifier.padding(padding)
        ) {
            composable(Rutas.RAZAS) {
                RazasScreen(onRazaClick = { nombre -> navController.navigate(Rutas.detalle(nombre)) })
            }
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument(Rutas.ARG_NOMBRE) { type = NavType.StringType })
            ) {
                DetalleRazaScreen(onVolver = { navController.popBackStack() })
            }
            composable(Rutas.CLIMA) { ClimaScreen() }
            composable(Rutas.GATOS) { GatosScreen() }
            composable(Rutas.FAVORITAS) {
                FavoritasScreen(onRazaClick = { nombre -> navController.navigate(Rutas.detalle(nombre)) })
            }
        }
    }
}

// Cambia de pestaña sin apilar pantallas repetidas y conservando el estado de cada una.
private fun navegarAPestana(navController: NavHostController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
