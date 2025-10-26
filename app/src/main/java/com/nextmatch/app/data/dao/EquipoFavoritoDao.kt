package com.nextmatch.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nextmatch.app.data.entities.EquipoFavoritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoFavoritoDao {
    @Insert
    suspend fun insertarFavorito(equipo: EquipoFavoritoEntity): Long

    @Update
    suspend fun actualizarFavorito(equipo: EquipoFavoritoEntity)

    @Delete
    suspend fun eliminarFavorito(equipo: EquipoFavoritoEntity)

    @Query("SELECT * FROM equipos_favoritos WHERE usuarioId = :usuarioId")
    fun obtenerFavoritosPorUsuario(usuarioId: Int): Flow<List<EquipoFavoritoEntity>>

    @Query("SELECT * FROM equipos_favoritos WHERE id = :id")
    suspend fun obtenerFavoritoPorId(id: Int): EquipoFavoritoEntity?

    @Query("SELECT * FROM equipos_favoritos WHERE usuarioId = :usuarioId AND equipoId = :equipoId")
    suspend fun obtenerFavoritoPorEquipo(usuarioId: Int, equipoId: Int): EquipoFavoritoEntity?

    @Query("DELETE FROM equipos_favoritos WHERE usuarioId = :usuarioId")
    suspend fun eliminarFavoritosPorUsuario(usuarioId: Int)

    @Query("SELECT COUNT(*) FROM equipos_favoritos WHERE usuarioId = :usuarioId")
    suspend fun contarFavoritosPorUsuario(usuarioId: Int): Int
}