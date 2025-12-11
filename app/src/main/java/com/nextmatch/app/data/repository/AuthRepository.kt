package com.nextmatch.app.data.repository

import com.nextmatch.app.data.remote.AuthApiService
import com.nextmatch.app.data.remote.BackendApiModule
import com.nextmatch.app.data.remote.UserApiService
import com.nextmatch.app.data.remote.dto.LoginRequestDto
import com.nextmatch.app.data.remote.dto.UpdateUserRequestDto
import com.nextmatch.app.data.remote.dto.UserRegistrationRequestDto
import com.nextmatch.app.model.UserProfile

// Implementa las llamadas remotas para registro, login y actualizacion de usuario.
class AuthRepository {
    private val authApi: AuthApiService = BackendApiModule.create(AuthApiService::class.java)
    private val userApi: UserApiService = BackendApiModule.create(UserApiService::class.java)

    // Genera el DTO de registro y devuelve el usuario creado.
    suspend fun registerUser(
        nombre: String,
        correo: String,
        clave: String,
        direccion: String
    ): Result<UserProfile> = runCatching {
        val dto = UserRegistrationRequestDto(nombre = nombre, correo = correo, clave = clave, direccion = direccion)
        userApi.register(dto).toDomain()
    }

    // Autentica credenciales y transforma la respuesta en UserProfile.
    suspend fun login(correo: String, clave: String): Result<UserProfile> = runCatching {
        authApi.login(LoginRequestDto(correo, clave)).toDomain()
    }

    // Actualiza datos basicos del usuario identificado por id.
    suspend fun updateUser(id: String, nombre: String, direccion: String): Result<UserProfile> = runCatching {
        val dto = UpdateUserRequestDto(nombre = nombre, direccion = direccion)
        userApi.update(id, dto).toDomain()
    }
}
