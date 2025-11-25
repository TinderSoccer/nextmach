package com.nextmatch.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.utils.GpsCalculator

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

// PANTALLA 7: CREAR EQUIPO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEquipoScreenCompose(navController: NavController) {
    val nombreEquipo = remember { mutableStateOf("") }
    val nivelEquipo = remember { mutableStateOf("Intermedio") }
    val conSuplentes = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Equipo") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombreEquipo.value,
                onValueChange = { nombreEquipo.value = it },
                label = { Text("Nombre del Equipo") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = colorResource(R.color.surface_dark)
                )
            )

            Text("Nivel del Equipo", color = colorResource(R.color.text_white))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Principiante", "Intermedio", "Avanzado").forEach { nivel ->
                    Button(
                        onClick = { nivelEquipo.value = nivel },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (nivelEquipo.value == nivel) colorResource(R.color.neon_green) else colorResource(R.color.surface_dark)
                        )
                    ) {
                        Text(nivel, fontSize = 12.sp, color = colorResource(R.color.background_black))
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = conSuplentes.value, onCheckedChange = { conSuplentes.value = it })
                Text("Incluir suplentes", color = colorResource(R.color.text_white))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
            ) {
                Text("Crear Equipo", color = colorResource(R.color.background_black), fontWeight = FontWeight.Bold)
            }
        }
    }
}

// PANTALLA 8: MAPA DE CANCHAS CON GPS NATIVO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaCanchasScreenCompose(navController: NavController) {
    // Ubicación del usuario actual (ejemplo: Buenos Aires, Argentina)
    val userLatitude = -34.6037
    val userLongitude = -58.3816

    // Datos de canchas con sus coordenadas GPS reales
    val canchas = listOf(
        Triple("Cancha del Centro", -34.6000, -58.3800),          // ~0.5 km
        Triple("Cancha La Boca", -34.6350, -58.3600),             // ~5.2 km
        Triple("Cancha Nordelta", -34.4817, -58.7542),            // ~32 km (para mostrar distancias largas)
    )

    val gpsCalculator = GpsCalculator()

    // Calcular distancias usando función nativa JNI en C
    val distancias = canchas.map { (nombre, lat, lon) ->
        val distancia = gpsCalculator.calcularDistanciaGPS(userLatitude, userLongitude, lat, lon)
        Triple(nombre, distancia, gpsCalculator.formatearDistancia(distancia))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Canchas") },
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
                .background(colorResource(R.color.background_black)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Información del mapa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.surface_dark))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📍 Mapa de Canchas", color = colorResource(R.color.neon_green), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Cálculos GPS en tiempo real (API nativa)", color = colorResource(R.color.neon_green), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tu ubicación: -34.60°S, -58.38°O", color = colorResource(R.color.text_medium_gray), fontSize = 11.sp)
                }
            }

            // Lista de canchas cercanas ordenadas por distancia
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(distancias.size) { index ->
                    val (nombre, distanciaKm, distanciaFormato) = distancias[index]
                    val estaCerca = distanciaKm < 5.0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("booking") },
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.surface_dark)
                        ),
                        border = if (estaCerca) {
                            BorderStroke(2.dp, colorResource(R.color.neon_green))
                        } else null
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    nombre,
                                    color = colorResource(R.color.neon_green),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    distanciaFormato,
                                    color = if (estaCerca) colorResource(R.color.neon_green) else colorResource(R.color.text_medium_gray),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                if (estaCerca) "✓ Cercana - Disponible hoy" else "Disponible hoy",
                                color = colorResource(R.color.text_medium_gray),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Nota sobre la tecnología
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark)),
                border = BorderStroke(1.dp, colorResource(R.color.neon_green))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🚀 GPS del dispositivo",
                        color = colorResource(R.color.neon_green),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        "Las distancias se calculan usando la API Location de Android para aprovechar el GPS nativo del equipo.",
                        color = colorResource(R.color.text_medium_gray),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// PANTALLA 9: PERFIL DE USUARIO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreenCompose(navController: NavController, authViewModel: com.nextmatch.app.viewmodel.AuthViewModel? = null) {
    val authState = authViewModel?.estado?.collectAsState()?.value
    val usuarioActual = authState?.usuarioActual

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Avatar y nombre
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.surface_dark), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(colorResource(R.color.neon_green), shape = RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 40.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(usuarioActual?.nombre ?: "Nombre", color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(usuarioActual?.correo ?: "correo@email.com", color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                }
            }

            item {
                // Estadísticas
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Estadísticas", color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard("Partidos", "12")
                        StatCard("Goles", "24")
                        StatCard("Rating", "4.8")
                    }
                }
            }

            item {
                // Botones
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))
                ) {
                    Text("Editar Perfil", color = colorResource(R.color.background_black))
                }
            }
        }
    }
}

@Composable
fun RowScope.StatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(colorResource(R.color.surface_dark), RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = colorResource(R.color.neon_green), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(title, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
    }
}

// PANTALLA 10: NOTIFICACIONES
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreenCompose(navController: NavController) {
    val mockNotificaciones = listOf(
        "Te han desafiado - Equipo Rojo" to "Hace 5 min",
        "Se aceptó tu solicitud - Equipo Azul" to "Hace 1 hora",
        "Nueva cancha disponible - Centro" to "Hace 2 horas",
        "Tu perfil fue visto - 3 usuarios" to "Hace 3 horas",
        "Recuerdo: Partido mañana 19:00" to "Hace 5 horas"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
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
                .background(colorResource(R.color.background_black)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mockNotificaciones.size) { index ->
                NotificationItem(
                    titulo = mockNotificaciones[index].first,
                    tiempo = mockNotificaciones[index].second
                )
            }
        }
    }
}

@Composable
fun NotificationItem(titulo: String, tiempo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(colorResource(R.color.neon_green), shape = RoundedCornerShape(50))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
                Text(tiempo, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
            }
        }
    }
}
