package com.nextmatch.app.data.remote.dto

import com.nextmatch.app.model.Team

data class TeamDto(
    val id: String?,
    val nombre: String,
    val ciudad: String?,
    val nivel: String?,
    val contacto: String?,
    val descripcion: String?,
    val userId: String? // New field
) {
    fun toDomain() = Team(
        id = id.orEmpty(),
        nombre = nombre,
        ciudad = ciudad,
        nivel = nivel,
        contacto = contacto,
        descripcion = descripcion,
        userId = userId
    )
}
