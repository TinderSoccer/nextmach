package com.nextmatch.app.teams

/**
 * Data class que representa un equipo en la aplicación
 * @param id Identificador único del equipo
 * @param name Nombre del equipo
 * @param location Ubicación del equipo
 * @param level Nivel de habilidad (estrellas 1-5)
 * @param playerCount Cantidad de jugadores en el equipo
 * @param avatarUrl URL de la imagen del avatar del equipo
 */
data class Team(
    val id: String,
    val name: String,
    val location: String,
    val level: Float = 3.0f,
    val playerCount: Int = 0,
    val avatarUrl: String? = null,
    val description: String = "",
    val goalsDifference: Int = 0,
    val matchesPlayed: Int = 0
)