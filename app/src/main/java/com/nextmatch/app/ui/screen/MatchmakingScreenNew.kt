package com.nextmatch.app.ui.screen

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.ui.components.OpenStreetMapView
import com.nextmatch.app.ui.components.OsmMarker
import com.nextmatch.app.utils.LocationTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random
import androidx.compose.runtime.MutableState // NEW IMPORT

@Composable
fun MatchmakingScreenNew(navController: NavController) {
    // Orquesta el flujo de emparejamiento via GPS y gestiona los permisos.
    val isSearching = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val userLocation = remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationTracker = remember(context.applicationContext) {
        LocationTracker(context.applicationContext)
    }
    val hasLocationPermission = remember { mutableStateOf(locationTracker.hasLocationPermission()) }
    val rivalMarkers = remember { mutableStateOf<List<OsmMarker>>(emptyList()) }

    // Lanzador que solicita permisos de ubicacion antes de buscar rivales.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission.value = granted
        if (granted) {
            scope.launch {
                performMatchmakingSearch(
                    errorMessage = errorMessage,
                    isSearching = isSearching,
                    userLocation = userLocation,
                    locationTracker = locationTracker,
                    rivalMarkers = rivalMarkers
                )
            }
        } else {
            errorMessage.value = "Permiso de ubicación requerido para calcular rivales."
        }
    }

    // Intenta obtener la ubicacion tan pronto se conceden los permisos.
    LaunchedEffect(hasLocationPermission.value) {
        if (hasLocationPermission.value && userLocation.value == null && !isSearching.value) {
            scope.launch {
                performMatchmakingSearch(
                    errorMessage = errorMessage,
                    isSearching = isSearching,
                    userLocation = userLocation,
                    locationTracker = locationTracker,
                    rivalMarkers = rivalMarkers
                )
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Emparejamiento GPS",
            style = MaterialTheme.typography.headlineSmall,
            color = colorResource(R.color.text_white),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Comparte tu ubicación para encontrar partidos cerca de ti",
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_medium_gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        MiniStatusCard(
            isSearching = isSearching.value,
            hasPermission = hasLocationPermission.value,
            errorMessage = errorMessage.value
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta con el mapa y los marcadores del usuario/rivales.
        MatchmakingMapCard(
            userMarker = userLocation.value?.let {
                OsmMarker(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    title = "Tu ubicación",
                    description = "Actualizada con GPS"
                )
            },
            rivalMarkers = rivalMarkers.value, // Now displays simulated rivals
            hasLocationPermission = hasLocationPermission.value,
            isSearching = isSearching.value,
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

        // Removed GpsResultCard as requested

        // CTA para disparar manualmente la busqueda nuevamente.
        PrimaryActionButton(
            text = if (isSearching.value) "Buscando..." else "Iniciar búsqueda",
            enabled = !isSearching.value,
            onClick = {
                if (hasLocationPermission.value) {
                    scope.launch {
                        performMatchmakingSearch(
                            errorMessage = errorMessage,
                            isSearching = isSearching,
                            userLocation = userLocation,
                            locationTracker = locationTracker,
                            rivalMarkers = rivalMarkers
                        )
                    }
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                isSearching.value = false
                errorMessage.value = null
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Volver", color = colorResource(R.color.neon_green), fontWeight = FontWeight.Bold)
        }
    }
}

// Refactored performSearch into a private suspend function
// Encapsula la logica de busqueda: obtener GPS, simular rivales y manejar errores.
private suspend fun performMatchmakingSearch(
    errorMessage: MutableState<String?>,
    isSearching: MutableState<Boolean>,
    userLocation: MutableState<Location?>,
    locationTracker: LocationTracker,
    rivalMarkers: MutableState<List<OsmMarker>>
) {
    errorMessage.value = null
    isSearching.value = true

    try {
        val location = locationTracker.getCurrentLocation()
        if (location == null) {
            errorMessage.value = "No se pudo obtener tu ubicación. Verifica el GPS."
            return // Returns from the suspend function
        }
        userLocation.value = location
        rivalMarkers.value = generateSimulatedOpponents(location)
    } catch (e: Exception) {
        errorMessage.value = "Error al usar el GPS: ${e.message}"
    } finally {
        isSearching.value = false
    }
}

// Contenedor visual para renderizar el mapa OSM y overlays de estado.
@Composable
private fun MatchmakingMapCard(
    userMarker: OsmMarker?,
    rivalMarkers: List<OsmMarker>,
    hasLocationPermission: Boolean,
    isSearching: Boolean,
    onRequestPermission: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 360.dp)
            .aspectRatio(1.1f),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.surface_dark)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            OpenStreetMapView(
                userMarker = userMarker,
                canchaMarkers = rivalMarkers, // Now displays simulated rivals
                modifier = Modifier.fillMaxSize(),
                zoom = 12.0
            )

            when {
                !hasLocationPermission -> {
                    MapOverlayBox {
                        Text(
                            text = "Concede el permiso de ubicación para ubicarte en el mapa.",
                            color = colorResource(R.color.text_white),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onRequestPermission) {
                            Text("Permitir ubicación")
                        }
                    }
                }

                isSearching -> {
                    MapOverlayBox {
                        CircularProgressIndicator(color = colorResource(R.color.neon_green))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Buscando tu posición...",
                            color = colorResource(R.color.text_white)
                        )
                    }
                }
            }

            if (hasLocationPermission && userMarker == null && !isSearching) {
                HintCard(
                    text = "Pulsa 'Buscar rival' para mostrar tu ubicación en tiempo real",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

// Resumen textual del estado del GPS/permisos al usuario.
@Composable
private fun MiniStatusCard(
    isSearching: Boolean,
    hasPermission: Boolean,
    errorMessage: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val status = when {
                isSearching -> "Buscando rivales cercanos"
                !hasPermission -> "Activa el GPS para continuar"
                else -> "Pulsá iniciar búsqueda para obtener tu ubicación"
            }
            Text(text = status, color = colorResource(R.color.text_white), fontWeight = FontWeight.Medium)
            if (!errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
        }
    }
}

// Crea una capa oscura sobre el mapa para mostrar mensajes temporales.
@Composable
private fun MapOverlayBox(content: @Composable ColumnScope.() -> Unit) {
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

// Boton reutilizable para acciones principales en la pantalla.
@Composable
private fun PrimaryActionButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.neon_green),
            contentColor = colorResource(R.color.background_black),
            disabledContainerColor = colorResource(R.color.neon_green).copy(alpha = 0.4f),
            disabledContentColor = colorResource(R.color.background_black)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

// Tarjeta liviana para instrucciones contextuales.
@Composable
private fun HintCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.background_black).copy(alpha = 0.75f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = colorResource(R.color.text_white),
            fontSize = 13.sp
        )
    }
}

// Genera rivales ficticios alrededor de la ubicacion del usuario para poblar el mapa.
private fun generateSimulatedOpponents(userLocation: Location): List<OsmMarker> {
    val simulatedOpponents = mutableListOf<OsmMarker>()
    val random = Random(System.currentTimeMillis()) // Seed random for consistent results per session if needed

    for (i in 1..5) { // Generate 5 simulated opponents
        val latOffset = (random.nextDouble() - 0.5) * 0.05 // +/- 0.025 degrees latitude
        val lonOffset = (random.nextDouble() - 0.5) * 0.05 // +/- 0.025 degrees longitude

        val rivalLat = userLocation.latitude + latOffset
        val rivalLon = userLocation.longitude + lonOffset
        val rivalName = "Rival ${i}"
        val rivalDescription = "Equipo ${i} cerca de ti"

        simulatedOpponents.add(
            OsmMarker(
                latitude = rivalLat,
                longitude = rivalLon,
                title = rivalName,
                description = rivalDescription
            )
        )
    }
    return simulatedOpponents
}
