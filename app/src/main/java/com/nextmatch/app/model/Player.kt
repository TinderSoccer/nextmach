package com.nextmatch.app.model

data class Player(
    val id: String,
    val nombre: String,
    val correo: String?,
    val posicion: String?,
    val nivel: String?, // Principiante, Intermedio, Avanzado
    val telefono: String?,
    val equipoId: String?, // ID del equipo al que pertenece
    val activo: Boolean = true
)
