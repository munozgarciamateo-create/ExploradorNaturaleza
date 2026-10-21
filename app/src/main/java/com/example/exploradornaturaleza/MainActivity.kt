package com.example.exploradornaturaleza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.exploradornaturaleza.ui.navegacion.AppNavegacion
import com.example.exploradornaturaleza.ui.theme.ExploradorNaturalezaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploradorNaturalezaTheme {
                AppNavegacion()
            }
        }
    }
}
