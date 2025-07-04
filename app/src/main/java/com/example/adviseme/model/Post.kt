package com.example.adviseme.model

data class Post(
    val id: String = "",                  // ← was Int, now String
    val userName: String,
    val content: String,
    val comments: MutableList<String> = mutableListOf()
)
