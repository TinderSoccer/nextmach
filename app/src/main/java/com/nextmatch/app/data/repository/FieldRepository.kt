package com.nextmatch.app.data.repository

import com.nextmatch.app.data.remote.BackendApiModule
import com.nextmatch.app.model.Field
import com.nextmatch.app.data.remote.FieldApiService

class FieldRepository {
    private val api: FieldApiService = BackendApiModule.create(FieldApiService::class.java)

    suspend fun fetchFields(): Result<List<Field>> = runCatching {
        api.getFields().map { it.toDomain() }
    }
}
