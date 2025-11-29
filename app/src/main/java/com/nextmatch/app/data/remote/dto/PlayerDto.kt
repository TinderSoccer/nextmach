package com.nextmatch.app.data.remote.dto

import com.nextmatch.app.model.Player

data class PlayerDto(
    val id: String?,
    val nombre: String,
    val correo: String?,
    val posicion: String?,
    val nivel: String?,
    val telefono: String?,
    val equipoId: String?,
    val activo: Boolean = true
) {
    fun toDomain() = Player(
        id = id.orEmpty(),
        nombre = nombre,
        correo = correo,
        posicion = posicion,
        nivel = nivel,
        telefono = telefono,
        equipoId = equipoId,
        activo = activo
    )
}
