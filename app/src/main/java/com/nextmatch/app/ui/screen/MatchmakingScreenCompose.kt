package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nextmatch.app.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchmakingScreenCompose(navController: NavController) {
    var progress by remember { mutableStateOf(0f) }
    var foundPlayers by remember { mutableStateOf(0) }
    var isSearching by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        repeat(100) {
            delay(50)
            progress = (it + 1) / 100f
            if (it % 25 == 0) {
                foundPlayers = (it / 25) + 1
            }
        }
        isSearching = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Oponentes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.surface_dark),
                    titleContentColor = colorResource(R.color.text_white)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(100.dp),
                color = colorResource(R.color.neon_green),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Buscando oponentes...",
                style = MaterialTheme.typography.titleLarge,
                color = colorResource(R.color.text_white)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isSearching) {
                Text(
                    text = "Conectando...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_medium_gray)
                )
            } else {
                Text(
                    text = "Jugadores encontrados: $foundPlayers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.neon_green)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navController.navigate("teams") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                enabled = !isSearching
            ) {
                Text("Ver Equipos Disponibles", color = colorResource(R.color.background_black))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark))
            ) {
                Text("Cancelar Búsqueda", color = colorResource(R.color.neon_green))
            }
        }
    }
}