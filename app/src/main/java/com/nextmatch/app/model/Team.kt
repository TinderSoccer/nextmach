package com.nextmatch.app.model

data class Team(
    val id: String,
    val nombre: String,
    val ciudad: String?,
    val nivel: String?,
    val contacto: String?,
    val descripcion: String?,
    val userId: String? = null // New field
)
