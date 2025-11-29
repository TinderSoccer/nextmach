package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nextmatch.app.data.remote.dto.CreateReservationRequestDto
import com.nextmatch.app.data.repository.ReservationRepository
import com.nextmatch.app.model.Reservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReservationUiState(
    val isLoading: Boolean = false,
    val reservation: Reservation? = null,
    val error: String? = null
)

class ReservationViewModel(private val repository: ReservationRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationUiState())
    val uiState: StateFlow<ReservationUiState> = _uiState.asStateFlow()

    fun createReservation(request: CreateReservationRequestDto) {
        viewModelScope.launch {
            _uiState.value = ReservationUiState(isLoading = true)
            repository.createReservation(request)
                .onSuccess { reservation ->
                    _uiState.value = ReservationUiState(isLoading = false, reservation = reservation)
                }
                .onFailure { throwable ->
                    _uiState.value = ReservationUiState(
                        isLoading = false,
                        error = throwable.message ?: "Error al crear la reserva"
                    )
                }
        }
    }

    fun clearStatus() {
        _uiState.value = ReservationUiState()
    }
}

class ReservationViewModelFactory(
    private val repository: ReservationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
