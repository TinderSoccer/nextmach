package com.nextmatch.app.data.remote.dto

data class CreateReservationRequestDto(
    val cancha: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val equipoId: String,
    val jugadorId: String? = null,
    val estado: String? = null,
    val notas: String? = null,
    val fieldId: String? = null
)
