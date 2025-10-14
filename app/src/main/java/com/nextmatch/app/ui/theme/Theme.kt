package com.nextmatch.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NextMatchGreen,
    onPrimary = Color.White,
    primaryContainer = NextMatchGreenLight,
    onPrimaryContainer = NeutralOnBackground,
    secondary = AccentOrange,
    onSecondary = Color.White,
    background = NeutralBackground,
    onBackground = NeutralOnBackground,
    surface = Color.White,
    onSurface = NeutralOnBackground,
    error = ErrorRed,
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = NextMatchGreenLight,
    onPrimary = NeutralOnBackground,
    primaryContainer = NextMatchGreenDark,
    onPrimaryContainer = Color.White,
    secondary = AccentOrange,
    onSecondary = NeutralOnBackground,
    background = Color(0xFF111827),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE2E8F0),
    error = ErrorRed,
    onError = Color(0xFF1F2937),
)

@Composable
fun NextMatchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
