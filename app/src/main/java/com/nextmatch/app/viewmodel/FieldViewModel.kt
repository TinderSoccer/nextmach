package com.nextmatch.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextmatch.app.data.repository.FieldRepository
import com.nextmatch.app.model.Field
import kotlinx.coroutines.launch

data class FieldUIState(
    val fields: List<Field> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Descarga la informacion de canchas y mantiene el estado Compose amigable.
class FieldViewModel(private val repository: FieldRepository = FieldRepository()) : ViewModel() {

    var uiState by mutableStateOf(FieldUIState())
        private set

    // Solicita las canchas al backend y actualiza la UI en consecuencia.
    fun fetchFields() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            repository.fetchFields()
                .onSuccess { fetchedFields ->
                    uiState = uiState.copy(isLoading = false, fields = fetchedFields)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(isLoading = false, error = throwable.message)
                }
        }
    }
}
