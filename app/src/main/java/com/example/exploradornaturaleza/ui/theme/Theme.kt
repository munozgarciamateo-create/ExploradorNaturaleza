package com.example.exploradornaturaleza.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val Verde = Color(0xFF2E7D32)
private val VerdeClaro = Color(0xFF81C784)

private val EsquemaClaro = lightColorScheme(primary = Verde, secondary = Color(0xFF558B2F))
private val EsquemaOscuro = darkColorScheme(primary = VerdeClaro, secondary = Color(0xFFAED581))

@Composable
fun ExploradorNaturalezaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Colores dinámicos en Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> EsquemaOscuro
        else -> EsquemaClaro
    }

    MaterialTheme(colorScheme = colorScheme, content = content)
}
