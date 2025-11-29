package com.nextmatch.app.utils

import com.nextmatch.app.data.entities.PlayerEntity
import com.nextmatch.app.data.remote.dto.PlayerDto

fun PlayerEntity.toDto() = PlayerDto(
    id = id,
    nombre = nombre,
    correo = correo,
    posicion = posicion,
    nivel = nivel,
    telefono = telefono,
    equipoId = equipoId,
    activo = activo
)

fun PlayerDto.toEntity() = PlayerEntity(
    id = id ?: throw IllegalStateException("PlayerDto ID cannot be null when converting to Entity"),
    nombre = nombre,
    correo = correo,
    posicion = posicion,
    nivel = nivel,
    telefono = telefono,
    equipoId = equipoId,
    activo = activo
)
