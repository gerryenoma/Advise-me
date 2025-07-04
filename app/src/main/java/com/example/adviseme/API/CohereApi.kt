package com.example.adviseme.api

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CohereApi {
    @Headers(
        "Authorization: ",
        "Content-Type: application/json",
        "Cohere-Version: 2022-12-06"
    )
    @POST("chat")
    suspend fun chat(@Body request: CohereChatRequest): CohereChatResponse
}
