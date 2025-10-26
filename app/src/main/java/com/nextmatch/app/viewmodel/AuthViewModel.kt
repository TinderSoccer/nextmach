package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextmatch.app.data.dao.UsuarioDao
import com.nextmatch.app.data.entities.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val usuarioActual: UsuarioEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAutenticado: Boolean = false
)

class AuthViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {
    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado

    /**
     * Intenta registrar un nuevo usuario
     */
    fun registrarUsuario(
        nombre: String,
        correo: String,
        clave: String,
        direccion: String
    ) {
        if (!validarDatos(nombre, correo, clave, direccion)) {
            _estado.value = _estado.value.copy(error = "Datos incompletos")
            return
        }

        viewModelScope.launch {
            try {
                _estado.value = _estado.value.copy(isLoading = true, error = null)

                // Verificar si el correo ya existe
                val usuarioExistente = usuarioDao.obtenerUsuarioPorCorreo(correo)
                if (usuarioExistente != null) {
                    _estado.value = _estado.value.copy(
                        error = "El correo ya está registrado",
                        isLoading = false
                    )
                    return@launch
                }

                // Crear nuevo usuario
                val nuevoUsuario = UsuarioEntity(
                    nombre = nombre,
                    correo = correo,
                    clave = clave, // En producción, hashear la contraseña
                    direccion = direccion
                )

                usuarioDao.insertarUsuario(nuevoUsuario)

                _estado.value = _estado.value.copy(
                    usuarioActual = nuevoUsuario,
                    isAutenticado = true,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _estado.value = _estado.value.copy(
                    error = "Error al registrar: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    /**
     * Intenta iniciar sesión con correo y contraseña
     */
    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isEmpty() || clave.isEmpty()) {
            _estado.value = _estado.value.copy(error = "Correo y contraseña requeridos")
            return
        }

        viewModelScope.launch {
            try {
                _estado.value = _estado.value.copy(isLoading = true, error = null)

                val usuario = usuarioDao.obtenerUsuarioPorCorreo(correo)

                if (usuario == null) {
                    _estado.value = _estado.value.copy(
                        error = "Usuario no encontrado",
                        isLoading = false
                    )
                    return@launch
                }

                if (usuario.clave != clave) {
                    _estado.value = _estado.value.copy(
                        error = "Contraseña incorrecta",
                        isLoading = false
                    )
                    return@launch
                }

                _estado.value = _estado.value.copy(
                    usuarioActual = usuario,
                    isAutenticado = true,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _estado.value = _estado.value.copy(
                    error = "Error al iniciar sesión: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    /**
     * Cierra la sesión actual
     */
    fun cerrarSesion() {
        _estado.value = AuthUiState()
    }

    /**
     * Actualiza los datos del usuario
     */
    fun actualizarUsuario(
        nombre: String,
        direccion: String
    ) {
        val usuarioActual = _estado.value.usuarioActual ?: return

        viewModelScope.launch {
            try {
                val usuarioActualizado = usuarioActual.copy(
                    nombre = nombre,
                    direccion = direccion,
                    updatedAt = System.currentTimeMillis()
                )
                usuarioDao.actualizarUsuario(usuarioActualizado)
                _estado.value = _estado.value.copy(usuarioActual = usuarioActualizado)
            } catch (e: Exception) {
                _estado.value = _estado.value.copy(error = "Error al actualizar: ${e.message}")
            }
        }
    }

    /**
     * Valida los datos básicos
     */
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