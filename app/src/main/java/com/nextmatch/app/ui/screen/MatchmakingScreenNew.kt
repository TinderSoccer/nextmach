package com.nextmatch.app.ui.screen

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nextmatch.app.R
import com.nextmatch.app.ui.components.LoadingSoccerBall
import com.nextmatch.app.ui.components.OpenStreetMapView
import com.nextmatch.app.ui.components.OsmMarker
import com.nextmatch.app.utils.GpsCalculator
import com.nextmatch.app.utils.LocationTracker
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MatchmakingScreenNew(navController: NavController) {
    val isSearching = remember { mutableStateOf(false) }
    val gpsResult = remember { mutableStateOf<GpsResult?>(null) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val userLocation = remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gpsCalculator = remember { GpsCalculator() }
    val locationTracker = remember(context.applicationContext) {
        LocationTracker(context.applicationContext)
    }
    val hasLocationPermission = remember { mutableStateOf(locationTracker.hasLocationPermission()) }
    val rivalMarkers = remember {
        listOf(
            OsmMarker(RIVAL_LATITUDE, RIVAL_LONGITUDE, "Rival premium", "Activo ahora"),
            OsmMarker(-12.0560, -77.0300, "Rival Miraflores", "Ping excelente"),
            OsmMarker(-12.0700, -77.0500, "Rival Centro", "Nivel avanzado")
        )
    }

    val performSearch: suspend () -> Unit = search@{
        errorMessage.value = null
        gpsResult.value = null
        isSearching.value = true

        try {
            val location = locationTracker.getCurrentLocation()
            if (location == null) {
                errorMessage.value = "No se pudo obtener tu ubicación. Verifica el GPS."
                return@search
            }
            userLocation.value = location

            val distanceKm = gpsCalculator.calcularDistanciaGPS(
                location.latitude,
                location.longitude,
                RIVAL_LATITUDE,
                RIVAL_LONGITUDE
            )
            val bearing = gpsCalculator.calcularAcimutGPS(
                location.latitude,
                location.longitude,
                RIVAL_LATITUDE,
                RIVAL_LONGITUDE
            )
            val midpoint = gpsCalculator.calcularPuntoMedioGPS(
                location.latitude,
                location.longitude,
                RIVAL_LATITUDE,
                RIVAL_LONGITUDE
            )

            gpsResult.value = GpsResult(
                userLat = location.latitude,
                userLon = location.longitude,
                rivalLat = RIVAL_LATITUDE,
                rivalLon = RIVAL_LONGITUDE,
                distanceText = gpsCalculator.formatearDistancia(distanceKm),
                bearingText = String.format(Locale.US, "%.1f°", bearing),
                midpointText = String.format(
                    Locale.US,
                    "lat=%.4f lon=%.4f",
                    midpoint.getOrNull(0) ?: 0.0,
                    midpoint.getOrNull(1) ?: 0.0
                )
            )
        } catch (e: Exception) {
            errorMessage.value = "Error al usar el GPS: ${e.message}"
        } finally {
            isSearching.value = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission.value = granted
        if (granted) {
            scope.launch { performSearch() }
        } else {
            errorMessage.value = "Permiso de ubicación requerido para calcular rivales."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn()
        ) {
            Text(
                text = "Emparejamiento automático",
                style = MaterialTheme.typography.headlineLarge,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        AnimatedVisibility(
            visible = isSearching.value,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            LoadingSoccerBall(modifier = Modifier.size(150.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        AnimatedVisibility(
            visible = isSearching.value || gpsResult.value != null || errorMessage.value != null,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            val statusText = when {
                isSearching.value -> "Buscando rival...\nObteniendo ubicación GPS nativa..."
                errorMessage.value != null -> errorMessage.value!!
                gpsResult.value != null -> "Rival encontrado usando la ubicación real."
                else -> ""
            }
            val statusColor = if (errorMessage.value != null) {
                MaterialTheme.colorScheme.error
            } else {
                colorResource(R.color.neon_green)
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = statusColor,
                fontSize = 14.sp
            )
        }

        gpsResult.value?.let {
            Spacer(modifier = Modifier.height(24.dp))
            GpsResultCard(result = it)
        }

        Spacer(modifier = Modifier.height(if (isSearching.value) 40.dp else 80.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Button(
                onClick = {
                    if (isSearching.value) return@Button
                    if (hasLocationPermission.value) {
                        scope.launch { performSearch() }
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSearching.value)
                        colorResource(R.color.neon_green).copy(alpha = 0.6f)
                    else
                        colorResource(R.color.neon_green)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isSearching.value
            ) {
                Text(
                    if (isSearching.value) "Buscando..." else "Buscar rival",
                    color = colorResource(R.color.background_black),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        MatchmakingMapCard(
            userMarker = userLocation.value?.let {
                OsmMarker(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    title = "Tu ubicación",
                    description = "Actualizada con GPS"
                )
            },
            rivalMarkers = rivalMarkers,
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

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            Button(
                onClick = {
                    isSearching.value = false
                    errorMessage.value = null
                    gpsResult.value = null
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.neon_green)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (isSearching.value) "Cancelar" else "Atrás",
                    color = colorResource(R.color.background_black),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

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
            .height(280.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.surface_dark)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            OpenStreetMapView(
                userMarker = userMarker,
                canchaMarkers = rivalMarkers,
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

                userMarker == null -> {
                    MapOverlayBox {
                        Text(
                            text = "Pulsa 'Buscar rival' para mostrar tu marcador.",
                            color = colorResource(R.color.text_white)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MapOverlayBox(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_black).copy(alpha = 0.65f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}

@Composable
private fun GpsResultCard(result: GpsResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.background_black).copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Tu ubicación",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = String.format(Locale.US, "lat=%.5f, lon=%.5f", result.userLat, result.userLon),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_white)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Distancia al rival",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.neon_green),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = result.distanceText,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.text_white)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Dirección",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.neon_green),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = result.bearingText,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(R.color.text_white)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Punto medio",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.neon_green),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = result.midpointText,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_white)
            )
        }
    }
}

private data class GpsResult(
    val userLat: Double,
    val userLon: Double,
    val rivalLat: Double,
    val rivalLon: Double,
    val distanceText: String,
    val bearingText: String,
    val midpointText: String
)

private const val RIVAL_LATITUDE = -12.0464
private const val RIVAL_LONGITUDE = -77.0428
