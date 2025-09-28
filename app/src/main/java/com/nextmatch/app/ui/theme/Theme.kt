package com.nextmatch.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.material3.lightColorScheme

// Esquema de colores con negro y verde neón
private val FootballColorScheme = lightColorScheme(
    primary = NeonGreen,     // Color principal → botones
    onPrimary = AppBackground, // Texto dentro de botones → negro
    background = AppBackground, // Fondo negro
    onBackground = AppWhite,   // Texto sobre fondo
    surface = AppBackground,   // Superficies negras
    onSurface = AppWhite       // Texto sobre superficies
)

@Composable
fun NextMatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FootballColorScheme,
        typography = AppTypography,
        shapes = Shapes(),
        content = content
    )
}
