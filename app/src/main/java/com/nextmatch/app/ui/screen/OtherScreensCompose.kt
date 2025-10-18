package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nextmatch.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsListScreenCompose(navController: NavController) {
    val mockTeams = listOf(
        "Equipo Rojo - ⭐⭐⭐⭐",
        "Equipo Azul - ⭐⭐⭐",
        "Equipo Verde - ⭐⭐⭐⭐⭐",
        "Equipo Amarillo - ⭐⭐",
        "Equipo Naranja - ⭐⭐⭐⭐"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipos Disponibles") },
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp)
        ) {
            items(mockTeams) { team ->
                Button(
                    onClick = { navController.navigate("team_profile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.surface_dark)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(team, color = colorResource(R.color.text_white))
                        Text("✓", color = colorResource(R.color.neon_green))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProfileScreenCompose(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Equipo") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.neon_green)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚽", style = MaterialTheme.typography.displayLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Equipo Premier", style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.text_white))
            Text("4.5 ⭐", color = colorResource(R.color.neon_green))

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("45", color = colorResource(R.color.neon_green), style = MaterialTheme.typography.titleLarge)
                    Text("Goles", color = colorResource(R.color.text_medium_gray), style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("12", color = colorResource(R.color.neon_green), style = MaterialTheme.typography.titleLarge)
                    Text("Partidos", color = colorResource(R.color.text_medium_gray), style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Solicitar Unirse", color = colorResource(R.color.background_black))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenCompose(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservar Cancha") },
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
            Text("Cancha del Sol", style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.text_white))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Santiago, Chile", color = colorResource(R.color.text_medium_gray))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("calendar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Seleccionar Fecha y Hora", color = colorResource(R.color.background_black))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenCompose(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seleccionar Horario") },
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
        val hours = listOf("18:00", "19:00", "20:00", "21:00")
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(24.dp)
        ) {
            Text("Horarios Disponibles", style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.text_white))
            Spacer(modifier = Modifier.height(24.dp))

            hours.forEach { hour ->
                Button(
                    onClick = { navController.navigate("confirmation") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(hour, color = colorResource(R.color.text_white))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreenCompose(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Reserva") },
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen de Reserva", style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.text_white))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cancha: Cancha del Sol", color = colorResource(R.color.text_white))
                    Text("Fecha: 28/11/2024", color = colorResource(R.color.text_white))
                    Text("Hora: 20:00 - 21:00", color = colorResource(R.color.text_white))
                    Text("Costo: \$120.000", color = colorResource(R.color.neon_green), style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("home") { popUpTo("home") { inclusive = false } } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirmar Reserva", color = colorResource(R.color.background_black))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreenCompose(navController: NavController) {
    val mockMessages = listOf("Equipo Rojo", "Equipo Azul", "Equipo Verde")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensajes") },
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_black))
                .padding(16.dp)
        ) {
            items(mockMessages) { chat ->
                Button(
                    onClick = { navController.navigate("chat") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.surface_dark)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(chat, color = colorResource(R.color.text_white))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenCompose(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
                        ) {
                            Text("Hola! ¿Quieres unirte al partido?", modifier = Modifier.padding(12.dp), color = colorResource(R.color.text_white))
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.neon_green))
                        ) {
                            Text("¡Claro! ¿A qué hora?", modifier = Modifier.padding(12.dp), color = colorResource(R.color.background_black))
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Escribir mensaje...") },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.surface_dark),
                        focusedContainerColor = colorResource(R.color.surface_dark)
                    )
                )
                Button(
                    onClick = {},
                    modifier = Modifier.size(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("→")
                }
            }
        }
    }
}