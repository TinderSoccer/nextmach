package com.nextmatch.app.data.remote

import com.nextmatch.app.data.remote.dto.CreateReservationRequestDto
import com.nextmatch.app.data.remote.dto.ReservationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationApiService {
    @GET("api/reservations")
    suspend fun getReservations(
        @Query("equipoId") equipoId: String? = null,
        @Query("fecha") fecha: String? = null
    ): List<ReservationDto>

    @POST("api/reservations")
    suspend fun createReservation(@Body request: CreateReservationRequestDto): ReservationDto
}
