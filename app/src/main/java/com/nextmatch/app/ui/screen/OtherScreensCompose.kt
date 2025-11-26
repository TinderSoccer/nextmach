package com.nextmatch.app.ui.screen

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.data.repository.FieldRepository
import com.nextmatch.app.model.Field
import com.nextmatch.app.ui.components.OpenStreetMapView
import com.nextmatch.app.ui.components.OsmMarker
import com.nextmatch.app.utils.GpsCalculator
import com.nextmatch.app.utils.LocationTracker
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val locationTracker = remember(context.applicationContext) { LocationTracker(context.applicationContext) }
    val hasLocationPermission = remember { mutableStateOf(locationTracker.hasLocationPermission()) }
    val userLocation = remember { mutableStateOf<Location?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission.value = granted
    }
    val gpsCalculator = remember { GpsCalculator() }
    val repository = remember { FieldRepository() }
    var fields by remember { mutableStateOf<List<Field>>(emptyList()) }
    var fieldsError by remember { mutableStateOf<String?>(null) }
    var loadingFields by remember { mutableStateOf(true) }
    var selectedField by remember { mutableStateOf<Field?>(null) }

    LaunchedEffect(hasLocationPermission.value) {
        if (hasLocationPermission.value) {
            userLocation.value = locationTracker.getCurrentLocation()
        }
    }

    LaunchedEffect(Unit) {
        loadingFields = true
        val result = repository.fetchFields()
        result.onSuccess {
            fields = it
            fieldsError = null
        }.onFailure { throwable ->
            fieldsError = throwable.message
        }
        loadingFields = false
    }

    val fieldMarkers = fields.map { field ->
        OsmMarker(field.latitud, field.longitud, field.nombre, field.direccion)
    }
    val fieldDistances = remember(fields, userLocation.value) {
        fields.map { field ->
            val distance = userLocation.value?.let { location ->
                gpsCalculator.calcularDistanciaGPS(
                    location.latitude,
                    location.longitude,
                    field.latitud,
                    field.longitud
                )
            }
            FieldWithDistance(field, distance)
        }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
    }

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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Elige la cancha más cercana",
                style = MaterialTheme.typography.headlineSmall,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Utilizamos tu GPS para ordenar las canchas por distancia",
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_medium_gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            BookingStatusCard(
                isSearching = loadingFields,
                hasPermission = hasLocationPermission.value,
                errorMessage = fieldsError,
                onRequestPermission = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BookingMapCard(
                userMarker = userLocation.value?.let {
                    OsmMarker(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        title = "Tu ubicación",
                        description = "Actualizada con GPS"
                    )
                },
                fieldMarkers = fieldMarkers,
                hasLocationPermission = hasLocationPermission.value,
                isSearching = loadingFields,
                onRequestPermission = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldSelectionList(
                fieldDistances = fieldDistances,
                selectedField = selectedField,
                onSelect = { field -> selectedField = field }
            )

            Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedField?.let { field ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedFieldName", field.nombre)
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedFieldId", field.id)
                    navController.navigate("calendar")
                }
            },
            enabled = selectedField != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.neon_green),
                contentColor = colorResource(R.color.background_black),
                disabledContainerColor = colorResource(R.color.neon_green).copy(alpha = 0.3f),
                disabledContentColor = colorResource(R.color.background_black)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (selectedField == null) "Selecciona una cancha" else "Elegir fecha y hora", fontWeight = FontWeight.Bold)
        }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver", color = colorResource(R.color.neon_green), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenCompose(navController: NavController) {
    val fieldName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("selectedFieldName")
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
            fieldName?.let {
                Text("Cancha seleccionada", color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                Text(it, style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.text_white))
                Spacer(modifier = Modifier.height(16.dp))
            }
            Text("Horarios Disponibles", style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.text_white))
            Spacer(modifier = Modifier.height(24.dp))

            hours.forEach { hour ->
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedFieldName", fieldName)
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedHour", hour)
                        navController.navigate("confirmation")
                    },
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
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val selectedFieldName = savedStateHandle?.get<String>("selectedFieldName")
    val selectedHour = savedStateHandle?.get<String>("selectedHour")
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
                    Text(
                        text = "Cancha: ${selectedFieldName ?: "Sin seleccionar"}",
                        color = colorResource(R.color.text_white)
                    )
                    Text(
                        text = "Hora: ${selectedHour ?: "Por definir"}",
                        color = colorResource(R.color.text_white)
                    )
                    Text("Costo estimado: \$120.000", color = colorResource(R.color.neon_green), style = MaterialTheme.typography.titleMedium)
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

private data class FieldWithDistance(val field: Field, val distanceKm: Double?)

@Composable
private fun FieldSelectionList(
    fieldDistances: List<FieldWithDistance>,
    selectedField: Field?,
    onSelect: (Field) -> Unit
) {
    if (fieldDistances.isEmpty()) {
        Text(
            text = "No se encontraron canchas registradas.",
            color = colorResource(R.color.text_medium_gray),
            modifier = Modifier.fillMaxWidth()
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        fieldDistances.forEach { item ->
            val isSelected = selectedField?.id == item.field.id
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onSelect(item.field) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        colorResource(R.color.surface_dark)
                    else
                        colorResource(R.color.background_black)
                ),
                border = if (isSelected) BorderStroke(1.dp, colorResource(R.color.neon_green)) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(item.field.nombre, color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
                    Text(item.field.direccion, color = colorResource(R.color.text_medium_gray), fontSize = 12.sp)
                    item.distanceKm?.let { distance ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatDistance(distance),
                            color = colorResource(R.color.neon_green),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingStatusCard(
    isSearching: Boolean,
    hasPermission: Boolean,
    errorMessage: String?,
    onRequestPermission: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val status = when {
                isSearching -> "Sincronizando canchas disponibles..."
                !hasPermission -> "Necesitamos tu permiso de ubicación"
                else -> "Selecciona una cancha para continuar"
            }
            Text(text = status, color = colorResource(R.color.text_white), fontWeight = FontWeight.Medium)
            if (!hasPermission) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onRequestPermission) {
                    Text("Activar GPS", color = colorResource(R.color.neon_green))
                }
            }
            if (!errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
        }
    }
}

private fun formatDistance(distanceKm: Double): String =
    if (distanceKm < 1) "${(distanceKm * 1000).toInt()} m" else String.format("%.2f km", distanceKm)

@Composable
private fun BookingMapCard(
    userMarker: OsmMarker?,
    fieldMarkers: List<OsmMarker>,
    hasLocationPermission: Boolean,
    isSearching: Boolean,
    onRequestPermission: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 320.dp)
            .aspectRatio(1.1f),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            OpenStreetMapView(
                userMarker = userMarker,
                canchaMarkers = fieldMarkers,
                modifier = Modifier.fillMaxSize(),
                zoom = 13.0
            )

            when {
                !hasLocationPermission -> {
                    BookingMapOverlay {
                        Text("Necesitamos tu permiso de ubicación", color = colorResource(R.color.text_white))
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRequestPermission) { Text("Activar GPS") }
                    }
                }
                isSearching -> {
                    BookingMapOverlay {
                        CircularProgressIndicator(color = colorResource(R.color.neon_green))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Buscando canchas...", color = colorResource(R.color.text_white))
                    }
                }
                fieldMarkers.isEmpty() -> {
                    BookingMapOverlay {
                        Text("Sin canchas registradas", color = colorResource(R.color.text_white))
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingMapOverlay(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black).copy(alpha = 0.65f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationTracker = remember(context.applicationContext) {
        LocationTracker(context.applicationContext)
    }
    val gpsCalculator = remember { GpsCalculator() }

    val userLocationState = remember { mutableStateOf<android.location.Location?>(null) }
    val isRequestingLocation = remember { mutableStateOf(false) }
    val permissionGranted = remember { mutableStateOf(locationTracker.hasLocationPermission()) }
    val permissionError = remember { mutableStateOf<String?>(null) }

    val canchas = remember {
        listOf(
            Cancha("Cancha Municipal Miraflores", -12.1211, -77.0305),
            Cancha("Estadio San Isidro", -12.1024, -77.0301),
            Cancha("Complejo La Molina", -12.0873, -76.9713),
            Cancha("Club San Borja", -12.0998, -76.9991),
            Cancha("Villa Deportiva Surco", -12.1394, -76.9931)
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        permissionGranted.value = granted
        if (granted) {
            permissionError.value = null
            scope.launch { loadUserLocation(locationTracker, userLocationState, isRequestingLocation) }
        } else {
            permissionError.value = "Se necesita el permiso para mostrar tu ubicación en el mapa."
        }
    }

    LaunchedEffect(permissionGranted.value) {
        if (permissionGranted.value && userLocationState.value == null && !isRequestingLocation.value) {
            loadUserLocation(locationTracker, userLocationState, isRequestingLocation)
        }
    }

    val originLat = userLocationState.value?.latitude ?: DEFAULT_LAT_LIMA
    val originLon = userLocationState.value?.longitude ?: DEFAULT_LON_LIMA

    val distancias = remember(originLat, originLon) {
        canchas.map { cancha ->
            val distancia = gpsCalculator.calcularDistanciaGPS(originLat, originLon, cancha.latitude, cancha.longitude)
            CanchaDistancia(
                cancha = cancha,
                distanciaKm = distancia,
                distanciaTexto = gpsCalculator.formatearDistancia(distancia)
            )
        }.sortedBy { it.distanciaKm }
    }

    val userMarker = userLocationState.value?.let {
        OsmMarker(
            latitude = it.latitude,
            longitude = it.longitude,
            title = "Tu ubicación",
            description = "Actualizada con GPS nativo"
        )
    }
    val canchaMarkers = canchas.map {
        OsmMarker(latitude = it.latitude, longitude = it.longitude, title = it.nombre, description = "Disponible hoy")
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
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                ) {
                    OpenStreetMapView(
                        userMarker = userMarker,
                        canchaMarkers = canchaMarkers,
                        modifier = Modifier.fillMaxSize(),
                        zoom = 12.0
                    )

                    if (!permissionGranted.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(R.color.background_black).copy(alpha = 0.75f))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Necesitamos tu permiso de ubicación para mostrar el mapa en tiempo real.",
                                color = colorResource(R.color.text_white),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }) {
                                Text("Conceder permiso")
                            }
                        }
                    } else if (isRequestingLocation.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(R.color.background_black).copy(alpha = 0.55f)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = colorResource(R.color.neon_green))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Obteniendo tu ubicación...", color = colorResource(R.color.text_white))
                        }
                    }
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
                    val distanciaInfo = distancias[index]
                    val estaCerca = distanciaInfo.distanciaKm < 5.0

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
                                    distanciaInfo.cancha.nombre,
                                    color = colorResource(R.color.neon_green),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    distanciaInfo.distanciaTexto,
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
                        "🗺️ OpenStreetMap + GPS del dispositivo",
                        color = colorResource(R.color.neon_green),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        "Renderizamos los mapas con OpenStreetMap (osmdroid) y calculamos distancias con la API de ubicación de Android para mantener todo nativo.",
                        color = colorResource(R.color.text_medium_gray),
                        fontSize = 11.sp
                    )
                    permissionError.value?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

private suspend fun loadUserLocation(
    locationTracker: LocationTracker,
    userLocationState: MutableState<Location?>,
    isRequestingLocation: MutableState<Boolean>
) {
    try {
        isRequestingLocation.value = true
        val location = locationTracker.getCurrentLocation()
        userLocationState.value = location
    } finally {
        isRequestingLocation.value = false
    }
}

private data class Cancha(
    val nombre: String,
    val latitude: Double,
    val longitude: Double
)

private data class CanchaDistancia(
    val cancha: Cancha,
    val distanciaKm: Double,
    val distanciaTexto: String
)

private const val DEFAULT_LAT_LIMA = -12.0464
private const val DEFAULT_LON_LIMA = -77.0428

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
