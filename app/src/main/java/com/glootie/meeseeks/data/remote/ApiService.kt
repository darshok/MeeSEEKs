package com.glootie.meeseeks.data.remote

import com.glootie.meeseeks.core.API_BASE_URL
import com.glootie.meeseeks.data.entity.response.CharacterDetailsResponse
import com.glootie.meeseeks.data.entity.response.CharacterPaginatedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(API_BASE_URL + "character/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterPaginatedResponse>

    @GET(API_BASE_URL + "character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): Response<CharacterDetailsResponse>
}