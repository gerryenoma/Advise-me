package com.example.adviseme.model

data class ChatMessage(
    val role: String,
    val content: String
)

/** Conversation document (root level). */
data class Conversation(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastSender: String = "",
    val lastTimestamp: Long = 0L
)

/** Individual message (stored in /conversations/{cid}/messages sub‑collection). */
data class Message(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)

