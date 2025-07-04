package com.example.adviseme.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CohereClient {
    private const val BASE_URL = "https://api.cohere.ai/v1/"

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: CohereApi = retrofit.create(CohereApi::class.java)
}
