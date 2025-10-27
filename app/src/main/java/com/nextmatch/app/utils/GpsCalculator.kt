package com.nextmatch.app.utils

class GpsCalculator {
    companion object {
        init {
            System.loadLibrary("nextmatch_gps")
        }
    }

    external fun calcularDistanciaGPS(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double
    external fun calcularAcimutGPS(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double
    external fun calcularPuntoMedioGPS(lat1: Double, lon1: Double, lat2: Double, lon2: Double): DoubleArray

    fun formatearDistancia(distanciaKm: Double): String {
        return if (distanciaKm < 1.0) {
            "${(distanciaKm * 1000).toInt()} m"
        } else {
            String.format("%.2f km", distanciaKm)
        }
    }

    fun estaCerca(lat1: Double, lon1: Double, lat2: Double, lon2: Double, maxKm: Double = 5.0): Boolean {
        return calcularDistanciaGPS(lat1, lon1, lat2, lon2) <= maxKm
    }
}