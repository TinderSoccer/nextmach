@file:OptIn(ExperimentalMaterial3Api::class)

package com.nextmatch.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nextmatch.app.R

@Composable
fun HomeScreen() {
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

            // Botón principal: Crear Partido
            Button(
                onClick = { /* TODO: Acción Crear Partido */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp) // Bordes redondeados
            ) {
                Text(
                    text = "Crear Partido",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp)) // Espacio entre botones

            // Botón secundario: Ver mis Partidos
            Button(
                onClick = { /* TODO: Acción Ver mis Partidos */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Ver mis Partidos",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Separación antes de Row adicional

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )


            // Row: ejemplo de elementos secundarios (iconos o botones)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón secundario 1
                Button(
                    onClick = { /* TODO: Acción secundaria 1 */ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text("Estadísticas")
                }

                // Botón secundario 2
                Button(
                    onClick = { /* TODO: Acción secundaria 2 */ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text("Perfil")
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espacio final
        }
    }
}
