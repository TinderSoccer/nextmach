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
    val playerMarkers = remember { emptyList<OsmMarker>() }

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

        MatchmakingMapCard(
            userMarker = userLocation.value?.let {
                OsmMarker(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    title = "Tu ubicación",
                    description = "Actualizada con GPS"
                )
            },
            rivalMarkers = playerMarkers,
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

        gpsResult.value?.let {
            GpsResultCard(result = it)
            Spacer(modifier = Modifier.height(16.dp))
        }

        PrimaryActionButton(
            text = if (isSearching.value) "Buscando..." else "Iniciar búsqueda",
            enabled = !isSearching.value,
            onClick = {
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
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                isSearching.value = false
                errorMessage.value = null
                gpsResult.value = null
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


@Composable
private fun StatBadge(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colorResource(R.color.background_black).copy(alpha = 0.5f),
        border = BorderStroke(1.dp, colorResource(R.color.neon_green).copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(it, contentDescription = null, tint = colorResource(R.color.neon_green))
            }
            Column {
                Text(text = title, color = colorResource(R.color.text_medium_gray), fontSize = 11.sp)
                Text(text = value, color = colorResource(R.color.text_white), fontWeight = FontWeight.Bold)
            }
        }
    }
}

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


@Composable
private fun GpsResultCard(result: GpsResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.surface_dark).copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, colorResource(R.color.neon_green).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Resumen GPS",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ubicación sincronizada con OpenStreetMap",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_medium_gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatBadge(
                    title = "Distancia",
                    value = result.distanceText
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatBadge(
                    title = "Dirección",
                    value = result.bearingText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Punto medio",
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_medium_gray)
            )
            Text(
                text = result.midpointText,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format(
                    Locale.US,
                    "Coordenadas actuales: %.5f / %.5f",
                    result.userLat,
                    result.userLon
                ),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_medium_gray)
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
