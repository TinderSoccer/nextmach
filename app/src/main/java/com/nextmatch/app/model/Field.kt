package com.nextmatch.app.model

data class Field(
    val id: String,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val descripcion: String? = null
)
