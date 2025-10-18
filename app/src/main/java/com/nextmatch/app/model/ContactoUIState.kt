package com.nextmatch.app.model

data class ContactoUiState(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val mensaje: String = "",
    val errores: ContactoErrores = ContactoErrores()
)
