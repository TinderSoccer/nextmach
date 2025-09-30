package com.nextmatch.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// Colores definidos en Color.kt
val FootballColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = AppBackground,
    background = AppBackground,
    onBackground = AppWhite,
    surface = AppBackground,
    onSurface = AppWhite
)

// Shapes
val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(0.dp)
)

@Composable
fun NextMatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FootballColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
