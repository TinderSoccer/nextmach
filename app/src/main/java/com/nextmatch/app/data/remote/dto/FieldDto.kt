package com.nextmatch.app.data.remote.dto

import com.nextmatch.app.model.Field

data class FieldDto(
    val id: String?,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val descripcion: String? = null
) {
    fun toDomain() = Field(
        id = id.orEmpty(),
        nombre = nombre,
        direccion = direccion,
        latitud = latitud,
        longitud = longitud,
        descripcion = descripcion
    )
}
