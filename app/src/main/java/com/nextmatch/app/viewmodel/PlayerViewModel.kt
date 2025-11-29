package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextmatch.app.data.entities.PlayerEntity
import com.nextmatch.app.data.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class PlayerUIState(
    val players: List<PlayerEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUIState(isLoading = true))
    val uiState: StateFlow<PlayerUIState> = _uiState.asStateFlow()

    private var currentTeamFilter: String? = null

    init {
        viewModelScope.launch {
            repository.players.collectLatest { players ->
                _uiState.value = _uiState.value.copy(players = players, isLoading = false, error = null)
            }
        }
        refreshPlayers(null)
    }

    fun refreshPlayers(equipoId: String?) {
        currentTeamFilter = equipoId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                repository.refreshPlayers(equipoId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun insertPlayer(player: PlayerEntity) {
        viewModelScope.launch {
            try {
                repository.insertPlayer(player)
                refreshPlayers(currentTeamFilter)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun updatePlayer(player: PlayerEntity) {
        viewModelScope.launch {
            try {
                repository.updatePlayer(player)
                refreshPlayers(currentTeamFilter)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun deletePlayer(player: PlayerEntity) {
        viewModelScope.launch {
            try {
                repository.deletePlayer(player)
                refreshPlayers(currentTeamFilter)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

}
