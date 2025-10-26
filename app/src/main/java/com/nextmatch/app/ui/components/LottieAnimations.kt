package com.nextmatch.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Animación de carga tipo pelota de fútbol (spinning soccer ball)
 * Usa una animación Lottie personalizada o fallback a una animación básica
 */
@Composable
fun LoadingSoccerBall(
    modifier: Modifier = Modifier.size(150.dp)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Intenta cargar composición de Lottie
        // Si no hay archivo JSON, mostrar una animación fallback
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(android.R.drawable.ic_menu_rotate)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = true,
            iterations = Int.MAX_VALUE,
            speed = 1f
        )

        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(150.dp)
            )
        } else {
            // Fallback a un Composable simple si no hay archivo
            FallbackLoadingAnimation()
        }
    }
}

/**
 * Animación de búsqueda activa (searching animation)
 * Muestra pulsos o radiaciones
 */
@Composable
fun SearchingAnimation(
    modifier: Modifier = Modifier.size(120.dp)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(android.R.drawable.ic_menu_search)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = true,
            iterations = Int.MAX_VALUE,
            speed = 1.5f
        )

        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.dp)
            )
        } else {
            FallbackSearchingAnimation()
        }
    }
}

/**
 * Animación de éxito/victoria (trophy or checkmark)
 * Se usa cuando se encuentra un rival o se completa una acción
 */
@Composable
fun SuccessAnimation(
    modifier: Modifier = Modifier.size(150.dp)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(android.R.drawable.ic_menu_view)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = true,
            iterations = 1,
            speed = 1.2f
        )

        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(150.dp)
            )
        } else {
            FallbackSuccessAnimation()
        }
    }
}

/**
 * Animación de error
 * Se usa para mostrar estados de error
 */
@Composable
fun ErrorAnimation(
    modifier: Modifier = Modifier.size(120.dp)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(android.R.drawable.ic_dialog_alert)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = true,
            iterations = Int.MAX_VALUE,
            speed = 1f
        )

        if (composition != null) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.dp)
            )
        }
    }
}

/**
 * Fallback para cuando no hay animación Lottie disponible
 */
@Composable
private fun FallbackLoadingAnimation() {
    // Implementar un composable simple para fallback
    androidx.compose.material3.CircularProgressIndicator(
        modifier = Modifier.size(100.dp),
        color = androidx.compose.ui.res.colorResource(id = com.nextmatch.app.R.color.neon_green),
        strokeWidth = 4.dp
    )
}

@Composable
private fun FallbackSearchingAnimation() {
    androidx.compose.material3.CircularProgressIndicator(
        modifier = Modifier.size(80.dp),
        color = androidx.compose.ui.res.colorResource(id = com.nextmatch.app.R.color.neon_green),
        strokeWidth = 3.dp
    )
}

@Composable
private fun FallbackSuccessAnimation() {
    androidx.compose.material3.Text(
        text = "✓",
        fontSize = androidx.compose.ui.unit.TextUnit(50f, androidx.compose.ui.unit.TextUnitType.Sp),
        color = androidx.compose.ui.res.colorResource(id = com.nextmatch.app.R.color.neon_green),
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
    )
}