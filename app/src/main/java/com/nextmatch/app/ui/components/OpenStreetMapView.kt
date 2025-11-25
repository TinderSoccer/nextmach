package com.nextmatch.app.ui.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(
    userMarker: OsmMarker?,
    canchaMarkers: List<OsmMarker>,
    modifier: Modifier = Modifier,
    zoom: Double = 13.0
) {
    val mapView = rememberMapViewWithLifecycle()

    AndroidView(
        modifier = modifier,
        factory = { mapView }
    ) { map ->
        updateMarkers(map, userMarker, canchaMarkers)
        val centerPoint = userMarker?.toGeoPoint()
            ?: canchaMarkers.firstOrNull()?.toGeoPoint()
            ?: DEFAULT_GEO_POINT
        map.controller.setZoom(zoom)
        map.controller.setCenter(centerPoint)
        map.invalidate()
    }
}

@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            isTilesScaledToDpi = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView.onPause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    return mapView
}

@SuppressLint("ClickableViewAccessibility")
private fun updateMarkers(
    mapView: MapView,
    userMarker: OsmMarker?,
    canchaMarkers: List<OsmMarker>
) {
    mapView.overlays.clear()

    userMarker?.let { marker ->
        mapView.overlays.add(marker.toMarker(mapView, isUser = true))
    }
    canchaMarkers.forEach { marker ->
        mapView.overlays.add(marker.toMarker(mapView, isUser = false))
    }
}

data class OsmMarker(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val description: String = ""
) {
    fun toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
}

private fun OsmMarker.toMarker(mapView: MapView, isUser: Boolean): Marker {
    return Marker(mapView).apply {
        position = toGeoPoint()
        title = this@toMarker.title
        snippet = description
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        if (isUser) {
            subDescription = "Ubicación actual"
        }
    }
}

private val DEFAULT_GEO_POINT = GeoPoint(-12.0464, -77.0428)
