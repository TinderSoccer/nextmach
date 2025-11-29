package com.nextmatch.app.data.remote.dto

import com.nextmatch.app.model.UserProfile

data class UserDto(
    val id: String,
    val nombre: String,
    val correo: String,
    val direccion: String
) {
    fun toDomain() = UserProfile(
        id = id,
        nombre = nombre,
        correo = correo,
        direccion = direccion
    )
}
