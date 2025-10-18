package com.nextmatch.app.ui.screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R

@Composable
fun MatchmakingScreenNew(navController: NavController) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000)),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Título
        Text(
            text = "Emparejamiento automático",
            style = MaterialTheme.typography.headlineLarge,
            color = colorResource(R.color.text_white),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Animación de carga circular
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    color = colorResource(R.color.background_black),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Círculo externo giratorio (anillo)
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .rotate(rotation)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(
                                colorResource(R.color.neon_green),
                                colorResource(R.color.neon_green)
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(12.dp)
                    .background(colorResource(R.color.background_black), CircleShape)
            )

            // Ícono o número en el centro
            Text(
                text = "⚽",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 64.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Mensaje de búsqueda
        Text(
            text = "Buscando rival disponible...",
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(R.color.text_white),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(80.dp))

        // Botones
        Button(
            onClick = { /* Reiniciar búsqueda */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Buscar rival",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Cancelar emparejamiento",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
