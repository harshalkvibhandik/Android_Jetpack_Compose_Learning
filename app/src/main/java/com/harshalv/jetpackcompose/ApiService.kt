package com.harshalv.jetpackcompose

import retrofit2.http.GET

interface ApiService {
    @GET("character")
    suspend fun getCharacters(): ApiResponse
}
