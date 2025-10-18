package com.nextmatch.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R

@Composable
fun LandingScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo grande
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de NextMatch",
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Título simple
        Text(
            text = "NextMatch",
            style = MaterialTheme.typography.headlineLarge,
            color = colorResource(R.color.neon_green),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón Ingresar
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Ingresar",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Registrar
        Button(
            onClick = { navController.navigate("registro") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Registrar",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}