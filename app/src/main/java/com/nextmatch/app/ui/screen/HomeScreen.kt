@file:OptIn(ExperimentalMaterial3Api::class)

package com.nextmatch.app.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                .padding(innerPadding) // Evita solaparse con la barra superior
                .fillMaxSize()          // Ocupa toda la pantalla
                .background(MaterialTheme.colorScheme.background) // Fondo del tema
                .padding(16.dp),        // Padding general de la pantalla
            verticalArrangement = Arrangement.Center, // Centrado vertical
            horizontalAlignment = Alignment.CenterHorizontally // Centrado horizontal
        ) {
            // Logo de la app
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de NextMatch",
                modifier = Modifier.size(200.dp) // Tamaño del logo
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio debajo del logo

            // Texto de bienvenida
            Text(
                text = "Bienvenido a NextMatch ⚽",
                style = MaterialTheme.typography.titleLarge, // Tipografía del tema
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del primer botón

            // Botón principal: Ir a Registro
            Button(
                onClick = { navController?.navigate("registro") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp) // Bordes redondeados
            ) {
                Text(
                    text = "Registro de Usuario",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre botones

            // Botón secundario: Formulario de Contacto
            Button(
                onClick = { navController?.navigate("contacto") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Formulario de Contacto",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Separación antes de Row adicional

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )


            Spacer(modifier = Modifier.height(12.dp))

            // Navegación adicional
            Button(
                onClick = { navController?.navigate("login") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController?.navigate("matchmaking") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Buscar Oponentes",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController?.navigate("teams") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Equipos Disponibles",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController?.navigate("booking") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Reservar Cancha",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController?.navigate("messages") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Mensajes",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
