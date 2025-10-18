package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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

@Composable
fun MatchFoundScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Alerta de título
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "¡Listo, tienes un partido!",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar del rival
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(colorResource(R.color.neon_green), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤",
                fontSize = 64.sp
            )
        }

        // Información del rival
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.surface_dark)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Representante
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Representante",
                        tint = colorResource(R.color.neon_green),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Representante",
                            color = colorResource(R.color.text_medium_gray),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Matías Rojas",
                            color = colorResource(R.color.text_white),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Divider(color = colorResource(R.color.background_black).copy(alpha = 0.3f))

                // Equipo
                Column {
                    Text(
                        text = "Equipo",
                        color = colorResource(R.color.text_medium_gray),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Los Galácticos FC",
                        color = colorResource(R.color.neon_green),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Divider(color = colorResource(R.color.background_black).copy(alpha = 0.3f))

                // Fecha y Hora
                Column {
                    Text(
                        text = "Fecha y Hora",
                        color = colorResource(R.color.text_medium_gray),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Viernes 5 de julio - 19:30 hrs",
                        color = colorResource(R.color.text_white),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Información de la cancha
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.surface_dark)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ubicación",
                        tint = colorResource(R.color.neon_green),
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Cancha",
                            color = colorResource(R.color.text_medium_gray),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Futbolito Ñuñoa",
                            color = colorResource(R.color.text_white),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = "Pedro de Valdivia 1234, Santiago",
                    color = colorResource(R.color.text_medium_gray),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de acción
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Confirmar partido",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

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
                "Cancelar partido",
                color = colorResource(R.color.background_black),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
