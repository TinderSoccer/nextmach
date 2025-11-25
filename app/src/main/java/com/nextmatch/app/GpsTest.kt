package com.nextmatch.app

import android.util.Log
import com.nextmatch.app.utils.GpsCalculator
import java.util.Locale

object GpsTest {
    private const val TAG = "GpsTest"

    fun demostrarGPS() {
        val gpsCalculator = GpsCalculator()

        val lat1 = 40.4168
        val lon1 = -3.7038
        val lat2 = 41.3851
        val lon2 = 2.1734

        val distanceKm = gpsCalculator.calcularDistanciaGPS(lat1, lon1, lat2, lon2)
        val azimuthDeg = gpsCalculator.calcularAcimutGPS(lat1, lon1, lat2, lon2)
        val midpoint = gpsCalculator.calcularPuntoMedioGPS(lat1, lon1, lat2, lon2)

        val distanceFormatted = gpsCalculator.formatearDistancia(distanceKm)
        val azimuthFormatted = String.format(Locale.US, "%.2f deg", azimuthDeg)
        val midpointFormatted = String.format(
            Locale.US,
            "lat=%.4f, lon=%.4f",
            midpoint.getOrNull(0) ?: Double.NaN,
            midpoint.getOrNull(1) ?: Double.NaN
        )

        Log.i(TAG, "Distancia: $distanceFormatted")
        Log.i(TAG, "Azimut: $azimuthFormatted")
        Log.i(TAG, "Punto medio: $midpointFormatted")
    }
}
