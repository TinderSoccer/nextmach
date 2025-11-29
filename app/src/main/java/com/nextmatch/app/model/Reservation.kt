package com.nextmatch.app.model

data class Reservation(
    val id: String,
    val cancha: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val equipoId: String,
    val jugadorId: String?,
    val estado: String,
    val notas: String?,
    val fieldId: String?
)
