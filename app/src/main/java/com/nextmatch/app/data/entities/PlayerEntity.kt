package com.nextmatch.app.data.entities

data class PlayerEntity(
    val id: String,
    val nombre: String,
    val correo: String?,
    val posicion: String?,
    val nivel: String?, // Principiante, Intermedio, Avanzado
    val telefono: String?,
    val equipoId: String?, // Foreign key to TeamEntity
    val activo: Boolean = true
)
