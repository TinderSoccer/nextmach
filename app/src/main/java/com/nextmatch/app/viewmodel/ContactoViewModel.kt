package com.nextmatch.app.viewmodel

import androidx.lifecycle.ViewModel
import com.nextmatch.app.model.ContactoErrores
import com.nextmatch.app.model.ContactoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ContactoViewModel : ViewModel() {

    private val _estado = MutableStateFlow(value = ContactoUiState())
    val estado: StateFlow<ContactoUiState> = _estado

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onEmailChange(valor: String) {
        _estado.update { it.copy(email = valor, errores = it.errores.copy(email = null)) }
    }

    fun onTelefonoChange(valor: String) {
        _estado.update { it.copy(telefono = valor, errores = it.errores.copy(telefono = null)) }
    }

    fun onMensajeChange(valor: String) {
        _estado.update { it.copy(mensaje = valor, errores = it.errores.copy(mensaje = null)) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = ContactoErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            email = if (!estadoActual.email.contains("@")) "Email inválido" else null,
            telefono = if (estadoActual.telefono.length < 8) "Teléfono inválido" else null,
            mensaje = if (estadoActual.mensaje.isBlank()) "Campo obligatorio" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.email,
            errores.telefono,
            errores.mensaje
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}
