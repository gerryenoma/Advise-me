package com.example.adviseme.data

import com.example.adviseme.model.Conversation
import com.example.adviseme.model.Message
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


object ChatRepository {

    private val db = Firebase.firestore
    private val auth get() = FirebaseAuth.getInstance()

    /* ---------- Helpers ---------- */

    /** Deterministic ID so the same pair of users always map to the same doc */
    private fun conversationIdFor(u1: String, u2: String): String =
        listOf(u1, u2).sorted().joinToString("_")

    /* ---------- Conversations ---------- */

    /** All conversations where the current user participates, real‑time updates. */
    fun conversationsFlow(): Flow<List<Conversation>> {
        val uid = auth.currentUser?.uid ?: return kotlinx.coroutines.flow.emptyFlow()
        return db.collection("conversations")
            .whereArrayContains("participants", uid)
            .orderBy("lastTimestamp")
            .snapshots()
            .map { snap ->
                snap.documents.mapNotNull { it.toObject(Conversation::class.java)?.copy(id = it.id) }
            }
    }

    /** Create a conversation if it doesn’t exist and return its ID. */
    suspend fun ensureConversationWith(otherUid: String): String {
        val myUid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
        val cid = conversationIdFor(myUid, otherUid)
        val docRef = db.collection("conversations").document(cid)
        db.runTransaction { trx ->
            if (!trx.get(docRef).exists()) {
                trx.set(
                    docRef,
                    Conversation(
                        id = cid,
                        participants = listOf(myUid, otherUid),
                        lastMessage = "",
                        lastSender = "",
                        lastTimestamp = System.currentTimeMillis()
                    )
                )
            }
        }.await()
        return cid
    }

    /* ---------- Messages ---------- */

    fun messagesFlow(conversationId: String): Flow<List<Message>> =
        db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp")
            .snapshots()
            .map { q ->
                q.documentChanges.filter { it.type != DocumentChange.Type.REMOVED }
                    .mapNotNull { it.document.toObject(Message::class.java).copy(id = it.document.id) }
            }

    suspend fun sendMessage(conversationId: String, text: String) {
        val uid = auth.currentUser?.uid ?: return
        val now = System.currentTimeMillis()
        val convRef = db.collection("conversations").document(conversationId)
        convRef.collection("messages").add(
            Message(
                senderId = uid,
                text = text,
                timestamp = now
            )
        )
        // Update the conversation header so it appears at the top of the list
        convRef.update(
            mapOf(
                "lastMessage" to text,
                "lastSender" to uid,
                "lastTimestamp" to now
            )
        )
    }
}
