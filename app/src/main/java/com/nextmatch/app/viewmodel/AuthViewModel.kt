package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextmatch.app.data.repository.AuthRepository
import com.nextmatch.app.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val usuarioActual: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAutenticado: Boolean = false
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado

    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        direccion: String
    ) {
        if (!validarDatos(nombre, correo, clave, direccion)) {
            _estado.update { it.copy(error = "Datos incompletos") }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(isLoading = true, error = null) }
            authRepository.registerUser(nombre, correo, clave, direccion)
                .onSuccess { user ->
                    _estado.update {
                        it.copy(
                            usuarioActual = user,
                            isAutenticado = true,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _estado.update { it.copy(error = throwable.message ?: "Error al registrar", isLoading = false) }
                }
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _estado.update { it.copy(error = "Correo y contraseña requeridos") }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(isLoading = true, error = null) }
            authRepository.login(correo, clave)
                .onSuccess { user ->
                    _estado.update {
                        it.copy(
                            usuarioActual = user,
                            isAutenticado = true,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _estado.update { it.copy(error = throwable.message ?: "Error al iniciar sesión", isLoading = false) }
                }
        }
    }

    fun cerrarSesion() {
        _estado.value = AuthUiState()
    }

    fun actualizarUsuario(nombre: String, direccion: String) {
        val usuarioActual = _estado.value.usuarioActual ?: return
        viewModelScope.launch {
            authRepository.updateUser(usuarioActual.id, nombre, direccion)
                .onSuccess { actualizado ->
                    _estado.update { it.copy(usuarioActual = actualizado, error = null) }
                }
                .onFailure { throwable ->
                    _estado.update { it.copy(error = throwable.message ?: "Error al actualizar") }
                }
        }
    }

    private fun validarDatos(
        nombre: String,
        correo: String,
        clave: String,
        direccion: String
    ): Boolean {
        return nombre.isNotBlank() &&
                correo.contains("@") &&
                clave.length >= 6 &&
                direccion.isNotBlank()
    }
}
