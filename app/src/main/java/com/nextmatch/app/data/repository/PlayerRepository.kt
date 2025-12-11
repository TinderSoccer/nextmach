package com.nextmatch.app.data.repository

import android.util.Log
import com.nextmatch.app.data.entities.PlayerEntity
import com.nextmatch.app.data.remote.PlayerApiService
import com.nextmatch.app.utils.toDto
import com.nextmatch.app.utils.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Administra las operaciones remotas y el cache en memoria de jugadores.
class PlayerRepository(
    private val playerApiService: PlayerApiService
) {

    private val _players = MutableStateFlow<List<PlayerEntity>>(emptyList())
    val players: StateFlow<List<PlayerEntity>> = _players.asStateFlow()

    // Consulta jugadores filtrados y actualiza el flujo local.
    suspend fun refreshPlayers(equipoId: String? = null) {
        try {
            val remotePlayers = playerApiService.getPlayers(equipoId).map { it.toEntity() }
            _players.value = remotePlayers
        } catch (e: Exception) {
            Log.e("PlayerRepository", "Error refreshing players: ${e.message}")
            throw e
        }
    }

    // Crea un jugador remoto y lo agrega al estado actual.
    suspend fun insertPlayer(player: PlayerEntity) {
        try {
            val createdPlayer = playerApiService.createPlayer(player.toDto()).toEntity()
            _players.value = _players.value + createdPlayer
        } catch (e: Exception) {
            Log.e("PlayerRepository", "Error creating player on remote: ${e.message}")
            throw e
        }
    }

    // Sincroniza un jugador existente con el backend.
    suspend fun updatePlayer(player: PlayerEntity) {
        try {
            val updatedPlayer = playerApiService.updatePlayer(player.id, player.toDto()).toEntity()
            _players.value = _players.value.map { if (it.id == updatedPlayer.id) updatedPlayer else it }
        } catch (e: Exception) {
            Log.e("PlayerRepository", "Error updating player on remote: ${e.message}")
            throw e
        }
    }

    // Elimina al jugador en el server y lo quita de la cache.
    suspend fun deletePlayer(player: PlayerEntity) {
        try {
            playerApiService.deletePlayer(player.id)
            _players.value = _players.value.filterNot { it.id == player.id }
        } catch (e: Exception) {
            Log.e("PlayerRepository", "Error deleting player on remote: ${e.message}")
            throw e
        }
    }
}
