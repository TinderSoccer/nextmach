package com.nextmatch.app.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial_busquedas",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistorialBusquedaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val equipoBuscadoNombre: String,
    val nivelJuego: String,
    val ubicacion: String,
    val encontrado: Boolean = false,
    val rivalEncontradoNombre: String? = null,
    val fechaBusqueda: Long = System.currentTimeMillis()
)