package com.nextmatch.app.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationTracker(context: Context) {

    private val appContext = context.applicationContext
    private val fusedClient = LocationServices.getFusedLocationProviderClient(appContext)

    fun hasLocationPermission(): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineGranted || coarseGranted
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) return null

        val tokenSource = CancellationTokenSource()
        val currentLocation = suspendCancellableCoroutine<Location?> { continuation ->
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                tokenSource.token
            ).addOnSuccessListener { location ->
                if (!continuation.isCompleted) {
                    continuation.resume(location)
                }
            }.addOnFailureListener {
                if (!continuation.isCompleted) {
                    continuation.resume(null)
                }
            }.addOnCanceledListener {
                if (!continuation.isCompleted) {
                    continuation.resume(null)
                }
            }

            continuation.invokeOnCancellation {
                tokenSource.cancel()
            }
        }

        if (currentLocation != null) return currentLocation

        return suspendCancellableCoroutine { continuation ->
            fusedClient.lastLocation
                .addOnSuccessListener { location ->
                    if (!continuation.isCompleted) {
                        continuation.resume(location)
                    }
                }
                .addOnFailureListener {
                    if (!continuation.isCompleted) {
                        continuation.resume(null)
                    }
                }
                .addOnCanceledListener {
                    if (!continuation.isCompleted) {
                        continuation.resume(null)
                    }
                }
        }
    }
}
