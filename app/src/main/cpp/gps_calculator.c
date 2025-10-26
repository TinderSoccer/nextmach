#include <jni.h>
#include <math.h>
#include <android/log.h>

#define LOG_TAG "NextMatchGPS"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// Constantes
#define EARTH_RADIUS_KM 6371.0
#define PI 3.14159265359
#define DEG_TO_RAD(deg) ((deg) * PI / 180.0)
#define RAD_TO_DEG(rad) ((rad) * 180.0 / PI)

/**
 * Calcula la distancia entre dos puntos GPS usando la fórmula de Haversine
 *
 * @param lat1 Latitud del primer punto en grados
 * @param lon1 Longitud del primer punto en grados
 * @param lat2 Latitud del segundo punto en grados
 * @param lon2 Longitud del segundo punto en grados
 * @return Distancia en kilómetros
 */
double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
    // Convertir grados a radianes
    double lat1_rad = DEG_TO_RAD(lat1);
    double lon1_rad = DEG_TO_RAD(lon1);
    double lat2_rad = DEG_TO_RAD(lat2);
    double lon2_rad = DEG_TO_RAD(lon2);

    // Diferencias
    double dlat = lat2_rad - lat1_rad;
    double dlon = lon2_rad - lon1_rad;

    // Fórmula de Haversine
    double a = sin(dlat / 2) * sin(dlat / 2) +
               cos(lat1_rad) * cos(lat2_rad) *
               sin(dlon / 2) * sin(dlon / 2);

    double c = 2 * asin(sqrt(a));
    double distance = EARTH_RADIUS_KM * c;

    return distance;
}

/**
 * Función JNI - Calcula distancia entre dos coordenadas GPS
 * Método Java: public native double calcularDistanciaGPS(double lat1, double lon1, double lat2, double lon2);
 */
JNIEXPORT jdouble JNICALL
Java_com_nextmatch_app_utils_GpsCalculator_calcularDistanciaGPS(
        JNIEnv *env,
        jobject obj,
        jdouble lat1,
        jdouble lon1,
        jdouble lat2,
        jdouble lon2) {

    double distance = calculateHaversineDistance(lat1, lon1, lat2, lon2);
    LOGI("Distancia calculada: %.2f km", distance);
    return distance;
}

/**
 * Función JNI - Calcula el acimut (ángulo) entre dos puntos GPS
 * Método Java: public native double calcularAcimutGPS(double lat1, double lon1, double lat2, double lon2);
 */
JNIEXPORT jdouble JNICALL
Java_com_nextmatch_app_utils_GpsCalculator_calcularAcimutGPS(
        JNIEnv *env,
        jobject obj,
        jdouble lat1,
        jdouble lon1,
        jdouble lat2,
        jdouble lon2) {

    // Convertir a radianes
    double lat1_rad = DEG_TO_RAD(lat1);
    double lon1_rad = DEG_TO_RAD(lon1);
    double lat2_rad = DEG_TO_RAD(lat2);
    double lon2_rad = DEG_TO_RAD(lon2);

    // Diferencia de longitud
    double dlon = lon2_rad - lon1_rad;

    // Cálculo del acimut
    double y = sin(dlon) * cos(lat2_rad);
    double x = cos(lat1_rad) * sin(lat2_rad) -
               sin(lat1_rad) * cos(lat2_rad) * cos(dlon);

    double acimut_rad = atan2(y, x);
    double acimut_deg = RAD_TO_DEG(acimut_rad);

    // Normalizar a 0-360 grados
    if (acimut_deg < 0) {
        acimut_deg += 360.0;
    }

    LOGI("Acimut calculado: %.2f grados", acimut_deg);
    return acimut_deg;
}

/**
 * Función JNI - Calcula punto intermedio entre dos coordenadas GPS
 * Método Java: public native double[] calcularPuntoMedioGPS(double lat1, double lon1, double lat2, double lon2);
 */
JNIEXPORT jdoubleArray JNICALL
Java_com_nextmatch_app_utils_GpsCalculator_calcularPuntoMedioGPS(
        JNIEnv *env,
        jobject obj,
        jdouble lat1,
        jdouble lon1,
        jdouble lat2,
        jdouble lon2) {

    // Convertir a radianes
    double lat1_rad = DEG_TO_RAD(lat1);
    double lon1_rad = DEG_TO_RAD(lon1);
    double lat2_rad = DEG_TO_RAD(lat2);
    double lon2_rad = DEG_TO_RAD(lon2);

    // Cálculo del punto medio
    double bx = cos(lat2_rad) * cos(lon2_rad - lon1_rad);
    double by = cos(lat2_rad) * sin(lon2_rad - lon1_rad);

    double lat_mid_rad = atan2(sin(lat1_rad) + sin(lat2_rad),
                               sqrt((cos(lat1_rad) + bx) * (cos(lat1_rad) + bx) + by * by));
    double lon_mid_rad = lon1_rad + atan2(by, cos(lat1_rad) + bx);

    // Convertir de nuevo a grados
    double lat_mid = RAD_TO_DEG(lat_mid_rad);
    double lon_mid = RAD_TO_DEG(lon_mid_rad);

    // Crear array para retornar [lat, lon]
    jdoubleArray result = (*env)->NewDoubleArray(env, 2);
    jdouble midpoint[2] = {lat_mid, lon_mid};
    (*env)->SetDoubleArrayRegion(env, result, 0, 2, midpoint);

    LOGI("Punto medio: lat=%.4f, lon=%.4f", lat_mid, lon_mid);
    return result;
}