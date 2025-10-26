package com.nextmatch.app.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservas",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val nombreCancha: String,
    val ubicacion: String,
    val fechaReserva: String, // Formato: "dd/MM/yyyy"
    val horaInicio: String, // Formato: "HH:mm"
    val horaFinal: String, // Formato: "HH:mm"
    val precio: Double,
    val estado: String, // Confirmada, Pendiente, Cancelada
    val cantidadJugadores: Int,
    val createdAt: Long = System.currentTimeMillis()
)