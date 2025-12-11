package com.nextmatch.app.data.repository

import com.nextmatch.app.data.remote.BackendApiModule
import com.nextmatch.app.data.remote.ReservationApiService
import com.nextmatch.app.data.remote.dto.CreateReservationRequestDto
import com.nextmatch.app.model.Reservation

// Maneja las operaciones de red relacionadas a las reservas de canchas.
class ReservationRepository {
    private val api: ReservationApiService = BackendApiModule.create(ReservationApiService::class.java)

    // Llama al endpoint de creacion y devuelve la reserva creada.
    suspend fun createReservation(request: CreateReservationRequestDto): Result<Reservation> = runCatching {
        api.createReservation(request).toDomain()
    }

    // Obtiene las reservas filtrando por equipo o fecha segun se requiera.
    suspend fun fetchReservations(equipoId: String? = null, fecha: String? = null): Result<List<Reservation>> = runCatching {
        api.getReservations(equipoId, fecha).map { it.toDomain() }
    }
}
