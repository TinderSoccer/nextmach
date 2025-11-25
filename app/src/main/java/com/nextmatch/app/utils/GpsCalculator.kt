package com.nextmatch.app.utils

import android.location.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GpsCalculator {

    fun calcularDistanciaGPS(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return (results.getOrNull(0) ?: 0f) / 1000.0
    }

    fun calcularAcimutGPS(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val results = FloatArray(2)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        var bearing = results.getOrNull(1)?.toDouble() ?: 0.0
        if (bearing < 0) {
            bearing += 360.0
        }
        return bearing
    }

    fun calcularPuntoMedioGPS(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): DoubleArray {
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val dLon = Math.toRadians(lon2 - lon1)

        val bx = cos(lat2Rad) * cos(dLon)
        val by = cos(lat2Rad) * sin(dLon)

        val latMid = atan2(
            sin(lat1Rad) + sin(lat2Rad),
            sqrt((cos(lat1Rad) + bx) * (cos(lat1Rad) + bx) + by * by)
        )
        val lonMid = lon1Rad + atan2(by, cos(lat1Rad) + bx)

        return doubleArrayOf(
            Math.toDegrees(latMid),
            Math.toDegrees(lonMid)
        )
    }

    fun formatearDistancia(distanciaKm: Double): String {
        return if (distanciaKm < 1.0) {
            "${(distanciaKm * 1000).toInt()} m"
        } else {
            String.format("%.2f km", distanciaKm)
        }
    }

    fun estaCerca(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        maxKm: Double = 5.0
    ): Boolean {
        return calcularDistanciaGPS(lat1, lon1, lat2, lon2) <= maxKm
    }
}
