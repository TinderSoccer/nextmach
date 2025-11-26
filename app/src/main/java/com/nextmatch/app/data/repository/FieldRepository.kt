package com.nextmatch.app.data.repository

import com.nextmatch.app.data.remote.FieldApiModule
import com.nextmatch.app.model.Field

class FieldRepository {
    private val api = FieldApiModule.service

    suspend fun fetchFields(): Result<List<Field>> = runCatching {
        api.getFields().map { it.toDomain() }
    }
}
