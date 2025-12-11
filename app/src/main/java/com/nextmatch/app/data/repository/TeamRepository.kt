package com.nextmatch.app.data.repository

import android.util.Log
import com.nextmatch.app.data.entities.TeamEntity
import com.nextmatch.app.data.remote.TeamApiService
import com.nextmatch.app.utils.toDto
import com.nextmatch.app.utils.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Fuente de datos que combina cache en memoria con el API de equipos.
class TeamRepository(
    private val teamApiService: TeamApiService
) {

    private val _teams = MutableStateFlow<List<TeamEntity>>(emptyList())
    val teams: StateFlow<List<TeamEntity>> = _teams.asStateFlow()

    // Sincroniza la lista local con el backend.
    suspend fun refreshTeams() {
        try {
            val remoteTeams = teamApiService.getTeams().map { it.toEntity() }
            _teams.value = remoteTeams
        } catch (e: Exception) {
            Log.e("TeamRepository", "Error fetching teams from remote: ${e.message}")
            throw e
        }
    }

    // Crea un equipo remoto y lo agrega al flujo interno.
    suspend fun insertTeam(team: TeamEntity) {
        try {
            val createdTeam = teamApiService.createTeam(team.toDto()).toEntity().copy(
                userId = team.userId
            )
            _teams.value = _teams.value + createdTeam
        } catch (e: Exception) {
            Log.e("TeamRepository", "Error creating team on remote: ${e.message}")
            throw e
        }
    }

    // Reemplaza un equipo existente con los datos devueltos por el servidor.
    suspend fun updateTeam(team: TeamEntity) {
        try {
            val updatedTeam = teamApiService.updateTeam(team.id, team.toDto()).toEntity().copy(
                userId = team.userId
            )
            _teams.value = _teams.value.map { if (it.id == updatedTeam.id) updatedTeam else it }
        } catch (e: Exception) {
            Log.e("TeamRepository", "Error updating team on remote: ${e.message}")
            throw e
        }
    }

    // Borra el equipo en el servidor y limpia la cache en memoria.
    suspend fun deleteTeam(team: TeamEntity) {
        try {
            teamApiService.deleteTeam(team.id)
            _teams.value = _teams.value.filterNot { it.id == team.id }
        } catch (e: Exception) {
            Log.e("TeamRepository", "Error deleting team on remote: ${e.message}")
            throw e
        }
    }

    // Permite recuperar rapidamente un equipo especifico desde memoria.
    fun getTeamById(id: String): TeamEntity? = _teams.value.find { it.id == id }
}
