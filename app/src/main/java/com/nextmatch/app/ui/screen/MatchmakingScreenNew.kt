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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val playersCatalog = remember { SAMPLE_PLAYERS }
    val playerMarkers = remember(playersCatalog) { playersCatalog.map { it.toMarker() } }
    val playersWithDistance = remember(userLocation.value) {
        playersCatalog.map { player ->
            val distance = userLocation.value?.let {
                gpsCalculator.calcularDistanciaGPS(
                    it.latitude,
                    it.longitude,
                    player.latitude,
                    player.longitude
                )
            }
            player.copy(distanceKm = distance)
        }.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
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

    val scrollState = rememberScrollState()
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            colorResource(R.color.background_black),
            colorResource(R.color.surface_dark)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
        MatchmakingHeroSection(
            isSearching = isSearching.value,
            gpsResult = gpsResult.value,
            errorMessage = errorMessage.value,
            hasPermission = hasLocationPermission.value,
            onRefresh = {
                if (!isSearching.value) {
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
            },
            onRequestPermission = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        gpsResult.value?.let {
            GpsResultCard(result = it)
            Spacer(modifier = Modifier.height(24.dp))
        }

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

        Spacer(modifier = Modifier.height(12.dp))

        MapLegend()

        Spacer(modifier = Modifier.height(20.dp))

        NearbyPlayersList(players = playersWithDistance)

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryActionButton(
                text = if (isSearching.value) "Buscando..." else "Buscar rival",
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

            SecondaryActionButton(
                text = if (isSearching.value) "Cancelar" else "Atrás",
                onClick = {
                    isSearching.value = false
                    errorMessage.value = null
                    gpsResult.value = null
                    navController.popBackStack()
                }
            )

        Spacer(modifier = Modifier.height(32.dp))
        }
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
private fun MatchmakingHeroSection(
    isSearching: Boolean,
    gpsResult: GpsResult?,
    errorMessage: String?,
    hasPermission: Boolean,
    onRefresh: () -> Unit,
    onRequestPermission: () -> Unit
) {
    val chipScroll = rememberScrollState()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.surface_dark).copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, colorResource(R.color.neon_green).copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Emparejamiento automático",
                style = MaterialTheme.typography.headlineSmall,
                color = colorResource(R.color.text_white),
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Juega con personas reales cercanas a ti usando tu GPS nativo",
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_medium_gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val statusText = when {
                        isSearching -> "Buscando rivales en tiempo real"
                        gpsResult != null -> "Ubicación sincronizada"
                        else -> "Listo para iniciar"
                    }
                    Text(
                        text = statusText,
                        color = colorResource(R.color.text_white),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (hasPermission) "GPS activado" else "Necesitamos tu permiso",
                        color = colorResource(R.color.text_medium_gray),
                        fontSize = 12.sp
                    )
                }

                if (isSearching) {
                    LoadingSoccerBall(modifier = Modifier.size(72.dp))
                } else {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Actualizar",
                            tint = colorResource(R.color.neon_green)
                        )
                    }
                }
            }

            if (!hasPermission) {
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryActionButton(
                    text = "Activar GPS",
                    onClick = onRequestPermission
                )
            }

            if (!errorMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(chipScroll),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("Menos de 5 km", "Nivel PRO", "Ping estable").forEach { label ->
                    HeroFilterChip(label)
                }
            }
        }
    }
}

@Composable
private fun HeroFilterChip(label: String) {
    AssistChip(
        onClick = {},
        label = { Text(label, color = colorResource(R.color.text_white)) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = colorResource(R.color.background_black).copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, colorResource(R.color.neon_green).copy(alpha = 0.3f))
    )
}

@Composable
private fun MapLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LegendItem(color = colorResource(R.color.neon_green), label = "Tu posición")
        LegendItem(color = colorResource(R.color.text_white), label = "Jugadores activos")
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = colorResource(R.color.text_medium_gray),
            fontSize = 12.sp
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
private fun SecondaryActionButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        border = BorderStroke(1.dp, colorResource(R.color.neon_green).copy(alpha = 0.6f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, color = colorResource(R.color.neon_green), fontWeight = FontWeight.SemiBold)
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
private fun NearbyPlayersList(players: List<NearbyPlayer>) {
    if (players.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.surface_dark)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Jugadores cercanos",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.neon_green),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val toShow = players.take(4)
            toShow.forEachIndexed { index, player ->
                PlayerRow(player)
                if (index < toShow.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = colorResource(R.color.background_black).copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun PlayerRow(player: NearbyPlayer) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PlayerAvatar(player.name)
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = player.name,
                    color = colorResource(R.color.text_white),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Text(
                    text = "${player.skill} • ${player.availability}",
                    color = colorResource(R.color.text_medium_gray),
                    fontSize = 12.sp
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = player.distanceLabel(),
                color = colorResource(R.color.neon_green),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Nivel ${player.rating}",
                color = colorResource(R.color.text_medium_gray),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun PlayerAvatar(name: String) {
    val letter = name.firstOrNull()?.uppercaseChar() ?: '?'
    Surface(
        modifier = Modifier.size(40.dp),
        shape = CircleShape,
        color = colorResource(R.color.background_black).copy(alpha = 0.6f),
        border = BorderStroke(1.dp, colorResource(R.color.neon_green).copy(alpha = 0.3f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = letter.toString(),
                color = colorResource(R.color.neon_green),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
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

private data class NearbyPlayer(
    val name: String,
    val skill: String,
    val rating: Float,
    val availability: String,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double? = null
)

private fun NearbyPlayer.toMarker() = OsmMarker(
    latitude = latitude,
    longitude = longitude,
    title = name,
    description = "$skill • $availability"
)

private fun NearbyPlayer.distanceLabel(): String {
    return distanceKm?.let {
        if (it < 1) "${(it * 1000).toInt()} m" else String.format(Locale.US, "%.1f km", it)
    } ?: "Pendiente"
}

private val SAMPLE_PLAYERS = listOf(
    NearbyPlayer("Carlos Díaz", "Delantero", 4.8f, "Disponible", -12.0495, -77.0330),
    NearbyPlayer("Luis Andrade", "Defensa", 4.5f, "Entrenando", -12.0582, -77.0221),
    NearbyPlayer("Mateo Silva", "Portero", 4.2f, "Disponible", -12.0618, -77.0502),
    NearbyPlayer("Jorge Ramos", "Volante", 4.7f, "En camino", -12.0402, -77.0280),
    NearbyPlayer("Sebastián León", "Delantero", 4.9f, "Listo para jugar", -12.0704, -77.0405)
)

private const val RIVAL_LATITUDE = -12.0464
private const val RIVAL_LONGITUDE = -77.0428
