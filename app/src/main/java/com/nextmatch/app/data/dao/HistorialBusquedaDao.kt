package com.nextmatch.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nextmatch.app.data.entities.HistorialBusquedaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialBusquedaDao {
    @Insert
    suspend fun insertarBusqueda(busqueda: HistorialBusquedaEntity): Long

    @Update
    suspend fun actualizarBusqueda(busqueda: HistorialBusquedaEntity)

    @Delete
    suspend fun eliminarBusqueda(busqueda: HistorialBusquedaEntity)

    @Query("SELECT * FROM historial_busquedas WHERE usuarioId = :usuarioId ORDER BY fechaBusqueda DESC")
    fun obtenerHistorialPorUsuario(usuarioId: Int): Flow<List<HistorialBusquedaEntity>>

    @Query("SELECT * FROM historial_busquedas WHERE id = :id")
    suspend fun obtenerBusquedaPorId(id: Int): HistorialBusquedaEntity?

    @Query("SELECT * FROM historial_busquedas WHERE usuarioId = :usuarioId AND encontrado = 1 ORDER BY fechaBusqueda DESC")
    fun obtenerBusquedasExitosas(usuarioId: Int): Flow<List<HistorialBusquedaEntity>>

    @Query("SELECT COUNT(*) FROM historial_busquedas WHERE usuarioId = :usuarioId AND encontrado = 1")
    suspend fun contarBusquedasExitosas(usuarioId: Int): Int

    @Query("DELETE FROM historial_busquedas WHERE usuarioId = :usuarioId")
    suspend fun limpiarHistorialPorUsuario(usuarioId: Int)

    @Query("SELECT * FROM historial_busquedas WHERE usuarioId = :usuarioId ORDER BY fechaBusqueda DESC LIMIT :limite")
    fun obtenerHistorialReciente(usuarioId: Int, limite: Int = 10): Flow<List<HistorialBusquedaEntity>>
}