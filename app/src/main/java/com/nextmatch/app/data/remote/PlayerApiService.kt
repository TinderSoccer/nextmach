package com.nextmatch.app.data.remote

import com.nextmatch.app.data.remote.dto.PlayerDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PlayerApiService {
    @GET("api/players")
    suspend fun getPlayers(@Query("equipoId") equipoId: String? = null): List<PlayerDto>

    @POST("api/players")
    suspend fun createPlayer(@Body player: PlayerDto): PlayerDto

    @PUT("api/players/{id}")
    suspend fun updatePlayer(@Path("id") id: String, @Body player: PlayerDto): PlayerDto

    @DELETE("api/players/{id}")
    suspend fun deletePlayer(@Path("id") id: String)
}
