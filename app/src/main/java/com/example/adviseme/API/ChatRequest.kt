package com.example.adviseme.model

data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<ApiMessage>
)
