package com.nextmatch.app.data.entities

data class TeamEntity(
    val id: String,
    val nombre: String,
    val ciudad: String?,
    val nivel: String?, // Principiante, Intermedio, Avanzado
    val contacto: String?,
    val descripcion: String?,
    val userId: String? = null // New field
)
