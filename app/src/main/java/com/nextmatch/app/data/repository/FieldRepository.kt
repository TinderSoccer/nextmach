package com.nextmatch.app.data.repository

import com.nextmatch.app.data.remote.BackendApiModule
import com.nextmatch.app.model.Field
import com.nextmatch.app.data.remote.FieldApiService

// Proveedor simple que consulta las canchas disponibles en el backend.
class FieldRepository {
    private val api: FieldApiService = BackendApiModule.create(FieldApiService::class.java)

    // Retorna un resultado seguro con la lista de canchas del API.
    suspend fun fetchFields(): Result<List<Field>> = runCatching {
        api.getFields().map { it.toDomain() }
    }
}
