package com.nextmatch.app.data.remote

import com.nextmatch.app.data.remote.dto.LoginRequestDto
import com.nextmatch.app.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): UserDto
}
