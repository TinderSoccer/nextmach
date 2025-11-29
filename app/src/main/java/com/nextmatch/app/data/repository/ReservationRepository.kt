package com.nextmatch.app.data.repository

import com.nextmatch.app.data.remote.BackendApiModule
import com.nextmatch.app.data.remote.ReservationApiService
import com.nextmatch.app.data.remote.dto.CreateReservationRequestDto
import com.nextmatch.app.model.Reservation

class ReservationRepository {
    private val api: ReservationApiService = BackendApiModule.create(ReservationApiService::class.java)

    suspend fun createReservation(request: CreateReservationRequestDto): Result<Reservation> = runCatching {
        api.createReservation(request).toDomain()
    }

    suspend fun fetchReservations(equipoId: String? = null, fecha: String? = null): Result<List<Reservation>> = runCatching {
        api.getReservations(equipoId, fecha).map { it.toDomain() }
    }
}
