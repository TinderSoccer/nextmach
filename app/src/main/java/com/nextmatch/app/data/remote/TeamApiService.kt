package com.nextmatch.app.data.remote

import com.nextmatch.app.data.remote.dto.TeamDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TeamApiService {
    @GET("api/teams")
    suspend fun getTeams(): List<TeamDto>

    @GET("api/teams/my")
    suspend fun getMyTeams(): List<TeamDto>

    @POST("api/teams")
    suspend fun createTeam(@Body team: TeamDto): TeamDto

    @PUT("api/teams/{id}")
    suspend fun updateTeam(@Path("id") id: String, @Body team: TeamDto): TeamDto

    @DELETE("api/teams/{id}")
    suspend fun deleteTeam(@Path("id") id: String)
}
