@file:OptIn(ExperimentalMaterial3Api::class)

package com.nextmatch.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nextmatch.app.R

@Composable
fun HomeScreen(navController: androidx.navigation.NavController? = null) {
    // Scaffold: estructura principal con TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NextMatch") } // Título de la app
            )
        }
    ) { innerPadding ->

        // Column: organiza elementos verticalmente
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo con animación de entrada
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de NextMatch",
                    modifier = Modifier.size(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título con animación
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { -30 }) + fadeIn()
            ) {
                Text(
                    text = "Menú Principal",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorResource(R.color.neon_green),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Buscar Oponentes - Botón 1
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("matchmaking") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.neon_green)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🔍 Buscar Oponentes",
                        color = colorResource(R.color.background_black),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            // Equipos Disponibles - Botón 2
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("teams") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.neon_green)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "👥 Equipos",
                        color = colorResource(R.color.background_black),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }



            // Reservar Cancha - Botón 3
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("booking") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.neon_green)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "⚽ Reservar Cancha",
                        color = colorResource(R.color.background_black),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            // Mensajes - Botón 4
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("messages") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.neon_green)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "💬 Mensajes",
                        color = colorResource(R.color.background_black),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = colorResource(R.color.text_white).copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mi Perfil - Botón 5
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("perfil_usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.surface_dark)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "👤 Mi Perfil",
                        color = colorResource(R.color.neon_green),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            // Mapa de Canchas - Botón 6
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("mapa_canchas") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.surface_dark)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🗺️ Mapa de Canchas",
                        color = colorResource(R.color.neon_green),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            // Notificaciones - Botón 7
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
            ) {
                Button(
                    onClick = { navController?.navigate("notificaciones") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.surface_dark)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🔔 Notificaciones",
                        color = colorResource(R.color.neon_green),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
