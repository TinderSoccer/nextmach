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

// Convierte los cambios del repositorio de equipos en un flujo listo para Compose.
class TeamViewModel(private val repository: TeamRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamUIState(isLoading = true))
    val uiState: StateFlow<TeamUIState> = _uiState.asStateFlow()

    init {
        // Suscribe la UI al stream local y dispara una primera carga remota.
        viewModelScope.launch {
            repository.teams.collectLatest { teams ->
                _uiState.value = _uiState.value.copy(teams = teams, isLoading = false, error = null)
            }
        }
        refreshTeams()
    }

    // Fuerza sincronizacion remota, mostrando progreso y errores.
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

    // Inserta un equipo y deja que el repositorio actualice el flujo.
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

    // Actualiza datos del equipo seleccionado en el servidor.
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

    // Elimina el equipo remoto y limpia el error si todo salio bien.
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

    // Busca rapidamente un equipo ya cacheado.
    fun getTeamById(id: String): TeamEntity? = repository.getTeamById(id)
}
