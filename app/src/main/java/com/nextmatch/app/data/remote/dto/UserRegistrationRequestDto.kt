package com.nextmatch.app.data.remote.dto

data class UserRegistrationRequestDto(
    val nombre: String,
    val correo: String,
    val clave: String,
    val direccion: String
)
