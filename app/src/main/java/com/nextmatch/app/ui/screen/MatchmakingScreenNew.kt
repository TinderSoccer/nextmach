package com.nextmatch.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.GpsTest
import com.nextmatch.app.ui.components.LoadingSoccerBall

@Composable
fun MatchmakingScreenNew(navController: NavController) {
    val isSearching = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

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

        AnimatedVisibility(
            visible = isSearching.value,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            LoadingSoccerBall(modifier = Modifier.size(150.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        AnimatedVisibility(
            visible = isSearching.value,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Text(
                text = "Buscando rival...\nCalculando distancias GPS...",
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.neon_green),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(if (isSearching.value) 40.dp else 80.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Button(
                onClick = {
                    if (!isSearching.value) {
                        isSearching.value = true
                        GpsTest.demostrarGPS()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSearching.value)
                        colorResource(R.color.neon_green).copy(alpha = 0.6f)
                    else
                        colorResource(R.color.neon_green)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isSearching.value
            ) {
                Text(
                    if (isSearching.value) "Buscando..." else "Buscar rival",
                    color = colorResource(R.color.background_black),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Button(
                onClick = {
                    isSearching.value = false
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.neon_green)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (isSearching.value) "Cancelar" else "Atrás",
                    color = colorResource(R.color.background_black),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
