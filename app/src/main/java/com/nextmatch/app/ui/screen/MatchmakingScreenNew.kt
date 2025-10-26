package com.nextmatch.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.ui.components.LoadingSoccerBall

@Composable
fun MatchmakingScreenNew(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Título con animación de entrada
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn()
        ) {
            Text(
                text = "Emparejamiento automático",
                style = MaterialTheme.typography.headlineLarge,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Animación de carga con Lottie (pelota de fútbol girando)
        LoadingSoccerBall(modifier = Modifier.size(150.dp))

        Spacer(modifier = Modifier.height(40.dp))

        // Mensaje de búsqueda con animación de entrada
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Text(
                text = "Buscando rival disponible...",
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.text_white),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Botón 1: Buscar rival con animación
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
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
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón 2: Cancelar con animación
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
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
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
