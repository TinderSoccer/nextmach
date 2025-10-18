package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R

@Composable
fun TeamsSelectionScreen(navController: NavController) {
    val equipos = listOf(
        "Los Cabros FC",
        "Los Macacos FC",
        "Los Galácticos FC",
        "Futbolito Kings",
        "Dream Team",
        "FC Champions",
        "Elite Soccer",
        "Barrio Legends"
    )

    var selectedTeam by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
    ) {
        // Header con fondo verde neón
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.neon_green))
                .padding(16.dp)
        ) {
            Text(
                text = "Buscando equipos...",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        // Título
        Text(
            text = "Equipos Disponibles",
            style = MaterialTheme.typography.headlineMedium,
            color = colorResource(R.color.text_white),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )

        // Lista de equipos
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(equipos) { equipo ->
                Button(
                    onClick = { selectedTeam = if (selectedTeam == equipo) null else equipo },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTeam == equipo)
                            colorResource(R.color.surface_dark)
                        else
                            colorResource(R.color.background_black)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.material3.ButtonDefaults.outlinedButtonBorder.copy(
                        brush = if (selectedTeam == equipo)
                            androidx.compose.ui.graphics.SolidColor(colorResource(R.color.neon_green))
                        else
                            androidx.compose.ui.graphics.SolidColor(colorResource(R.color.surface_dark))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = equipo,
                            color = colorResource(R.color.text_white),
                            fontWeight = FontWeight.Medium
                        )

                        if (selectedTeam == equipo) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Seleccionado",
                                tint = colorResource(R.color.neon_green),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de emparejamiento
        Button(
            onClick = {
                if (selectedTeam != null) {
                    navController.navigate("match_found")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = selectedTeam != null
        ) {
            Text(
                "Emparejar",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
