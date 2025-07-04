package com.example.adviseme.api

data class CohereChatRequest(
    val message: String,
    val model: String = "command-r",
    val temperature: Double = 0.7
)

