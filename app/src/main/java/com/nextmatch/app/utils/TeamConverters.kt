package com.nextmatch.app.utils

import com.nextmatch.app.data.entities.TeamEntity
import com.nextmatch.app.data.remote.dto.TeamDto
import java.util.UUID // Import UUID if not already imported elsewhere

fun TeamEntity.toDto() = TeamDto(
    id = id,
    nombre = nombre,
    ciudad = ciudad,
    nivel = nivel,
    contacto = contacto,
    descripcion = descripcion,
    userId = userId // Include userId
)

fun TeamDto.toEntity() = TeamEntity(
    id = id ?: throw IllegalStateException("TeamDto ID cannot be null when converting to Entity"),
    nombre = nombre,
    ciudad = ciudad,
    nivel = nivel,
    contacto = contacto,
    descripcion = descripcion,
    userId = userId // Include userId
)
