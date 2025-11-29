package com.nextmatch.app.data.remote

import com.nextmatch.app.data.remote.dto.UpdateUserRequestDto
import com.nextmatch.app.data.remote.dto.UserDto
import com.nextmatch.app.data.remote.dto.UserRegistrationRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    @POST("api/users")
    suspend fun register(@Body request: UserRegistrationRequestDto): UserDto

    @PUT("api/users/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body request: UpdateUserRequestDto
    ): UserDto
}
