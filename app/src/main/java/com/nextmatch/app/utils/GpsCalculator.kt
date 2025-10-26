package com.nextmatch.app.utils

/**
 * Clase que proporciona funciones nativas JNI para cálculos GPS
 * Utiliza código C compilado con NDK para cálculos de distancia y ubicación
 */
class GpsCalculator {
    companion object {
        // Cargar la librería nativa compilada desde C
        init {
            System.loadLibrary("nextmatch_gps")
        }
    }

    /**
     * Calcula la distancia entre dos puntos GPS usando la fórmula de Haversine
     * Esta función está implementada en C para mayor rendimiento
     *
     * @param lat1 Latitud del primer punto en grados (-90 a 90)
     * @param lon1 Longitud del primer punto en grados (-180 a 180)
     * @param lat2 Latitud del segundo punto en grados (-90 a 90)
     * @param lon2 Longitud del segundo punto en grados (-180 a 180)
     * @return Distancia en kilómetros
     *
     * Ejemplo:
     * val distancia = calcularDistanciaGPS(-33.8688, -51.5325, -33.8736, -51.5264)
     * // Retorna la distancia en km entre dos puntos de Rosario
     */
    external fun calcularDistanciaGPS(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double

    /**
     * Calcula el acimut (ángulo de brújula) entre dos puntos GPS
     * Retorna el ángulo en grados (0-360) donde:
     * - 0° = Norte
     * - 90° = Este
     * - 180° = Sur
     * - 270° = Oeste
     *
     * @param lat1 Latitud del primer punto en grados
     * @param lon1 Longitud del primer punto en grados
     * @param lat2 Latitud del segundo punto en grados
     * @param lon2 Longitud del segundo punto en grados
     * @return Acimut en grados (0-360)
     */
    external fun calcularAcimutGPS(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double

    /**
     * Calcula el punto intermedio (punto medio) entre dos coordenadas GPS
     * Útil para encontrar un punto de encuentro entre dos ubicaciones
     *
     * @param lat1 Latitud del primer punto en grados
     * @param lon1 Longitud del primer punto en grados
     * @param lat2 Latitud del segundo punto en grados
     * @param lon2 Longitud del segundo punto en grados
     * @return Array de Double con [latitud, longitud] del punto medio
     */
    external fun calcularPuntoMedioGPS(lat1: Double, lon1: Double, lat2: Double, lon2: Double): DoubleArray

    /**
     * Convierte kilómetros a metros
     * Función auxiliar en Kotlin (no necesita JNI)
     */
    fun kmAMetros(km: Double): Double = km * 1000

    /**
     * Convierte kilómetros a metros, redondeado a 0 decimales
     * Útil para mostrar distancias cortas en metros
     */
    fun kmAMetrosRedondeado(km: Double): Int = (km * 1000).toInt()

    /**
     * Formatea la distancia para mostrar en UI
     * Si es < 1 km muestra en metros, si no en km con 2 decimales
     */
    fun formatearDistancia(distanciaKm: Double): String {
        return if (distanciaKm < 1.0) {
            "${kmAMetrosRedondeado(distanciaKm)} m"
        } else {
            String.format("%.2f km", distanciaKm)
        }
    }

    /**
     * Verifica si dos ubicaciones son cercanas (dentro de cierta distancia)
     * Útil para determinar si un jugador está cerca de una cancha
     *
     * @param lat1 Latitud del primer punto
     * @param lon1 Longitud del primer punto
     * @param lat2 Latitud del segundo punto
     * @param lon2 Longitud del segundo punto
     * @param distanciaMaximaKm Distancia máxima en km (por defecto 5 km)
     * @return true si la distancia es menor o igual a distanciaMaximaKm
     */
    fun estaCerca(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        distanciaMaximaKm: Double = 5.0
    ): Boolean {
        val distancia = calcularDistanciaGPS(lat1, lon1, lat2, lon2)
        return distancia <= distanciaMaximaKm
    }
}