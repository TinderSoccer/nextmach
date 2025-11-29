package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nextmatch.app.data.entities.TeamEntity
import com.nextmatch.app.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// UI State for Team operations (optional, but good practice)
data class TeamUIState(
    val teams: List<TeamEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TeamViewModel(private val repository: TeamRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamUIState(isLoading = true))
    val uiState: StateFlow<TeamUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.teams.collectLatest { teams ->
                _uiState.value = _uiState.value.copy(teams = teams, isLoading = false, error = null)
            }
        }
        refreshTeams()
    }

    fun refreshTeams() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                repository.refreshTeams()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun insertTeam(team: TeamEntity) {
        viewModelScope.launch {
            try {
                repository.insertTeam(team)
                _uiState.value = _uiState.value.copy(error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateTeam(team: TeamEntity) {
        viewModelScope.launch {
            try {
                repository.updateTeam(team)
                _uiState.value = _uiState.value.copy(error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteTeam(team: TeamEntity) {
        viewModelScope.launch {
            try {
                repository.deleteTeam(team)
                _uiState.value = _uiState.value.copy(error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun getTeamById(id: String): TeamEntity? = repository.getTeamById(id)
}
