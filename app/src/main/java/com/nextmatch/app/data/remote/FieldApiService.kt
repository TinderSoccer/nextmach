package com.nextmatch.app.data.remote

import com.nextmatch.app.data.remote.dto.FieldDto
import retrofit2.http.GET

interface FieldApiService {
    @GET("api/fields")
    suspend fun getFields(): List<FieldDto>
}
