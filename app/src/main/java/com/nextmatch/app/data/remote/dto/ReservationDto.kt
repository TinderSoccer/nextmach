package com.nextmatch.app.data.remote.dto

import com.nextmatch.app.model.Reservation

data class ReservationDto(
    val id: String?,
    val cancha: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val equipoId: String,
    val jugadorId: String?,
    val estado: String?,
    val notas: String?,
    val fieldId: String?
) {
    fun toDomain() = Reservation(
        id = id.orEmpty(),
        cancha = cancha,
        fecha = fecha,
        horaInicio = horaInicio,
        horaFin = horaFin,
        equipoId = equipoId,
        jugadorId = jugadorId,
        estado = estado ?: "PENDIENTE",
        notas = notas,
        fieldId = fieldId
    )
}
