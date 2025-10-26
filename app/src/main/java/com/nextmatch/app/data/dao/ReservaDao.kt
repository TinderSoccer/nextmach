package com.nextmatch.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nextmatch.app.data.entities.ReservaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {
    @Insert
    suspend fun insertarReserva(reserva: ReservaEntity): Long

    @Update
    suspend fun actualizarReserva(reserva: ReservaEntity)

    @Delete
    suspend fun eliminarReserva(reserva: ReservaEntity)

    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId ORDER BY createdAt DESC")
    fun obtenerReservasPorUsuario(usuarioId: Int): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reservas WHERE id = :id")
    suspend fun obtenerReservaPorId(id: Int): ReservaEntity?

    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId AND estado = :estado ORDER BY createdAt DESC")
    fun obtenerReservasPorEstado(usuarioId: Int, estado: String): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId AND estado = 'Confirmada' ORDER BY createdAt DESC")
    fun obtenerReservasConfirmadas(usuarioId: Int): Flow<List<ReservaEntity>>

    @Query("SELECT COUNT(*) FROM reservas WHERE usuarioId = :usuarioId AND estado = :estado")
    suspend fun contarReservasPorEstado(usuarioId: Int, estado: String): Int

    @Query("DELETE FROM reservas WHERE usuarioId = :usuarioId AND estado = 'Cancelada'")
    suspend fun eliminarReservasCanceladas(usuarioId: Int)

    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId ORDER BY createdAt DESC LIMIT :limite")
    fun obtenerReservasRecientes(usuarioId: Int, limite: Int = 5): Flow<List<ReservaEntity>>
}