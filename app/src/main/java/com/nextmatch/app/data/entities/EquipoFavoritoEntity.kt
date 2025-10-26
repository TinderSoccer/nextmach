package com.nextmatch.app.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "equipos_favoritos",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EquipoFavoritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val equipoId: Int,
    val nombre: String,
    val ubicacion: String,
    val nivel: String, // Principiante, Intermedio, Avanzado
    val cantidadJugadores: Int,
    val urlAvatar: String,
    val descripcion: String,
    val createdAt: Long = System.currentTimeMillis()
)