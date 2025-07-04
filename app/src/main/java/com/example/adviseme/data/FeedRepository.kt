package com.example.adviseme.data

import android.util.Log
import com.example.adviseme.model.Post
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

object FeedRepository {

    private val db = FirebaseFirestore.getInstance()
    private val postsRef = db.collection("posts")
    private const val TAG = "FeedRepository"

    /** Safely fetch newest posts first */
    suspend fun fetchPosts(): List<Post> = try {
        val snap = postsRef
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        snap.documents.map { doc ->
            Post(
                id = doc.id,
                userName = doc.getString("userName") ?: "Unknown",
                content = doc.getString("content") ?: "",
                comments = (doc.get("comments") as? List<String> ?: emptyList()).toMutableList()
            )
        }
    } catch (e: FirebaseFirestoreException) {
        Log.e(TAG, "Failed to fetch posts", e)
        emptyList()
    }

    /** Add a new post to Firestore */
    suspend fun addPost(userName: String, content: String) = try {
        val data = mapOf(
            "userName" to userName,
            "content" to content,
            "comments" to emptyList<String>(),
            "timestamp" to Timestamp.now()
        )
        postsRef.add(data).await()
    } catch (e: FirebaseFirestoreException) {
        Log.e(TAG, "Failed to add post", e)
    }

    /** Add a comment to a post (atomic transaction) */
    suspend fun addComment(postId: String, comment: String) = try {
        val doc = postsRef.document(postId)
        db.runTransaction { tx ->
            val current = tx.get(doc).get("comments") as? List<String> ?: emptyList()
            tx.update(doc, "comments", current + comment)
        }.await()
    } catch (e: FirebaseFirestoreException) {
        Log.e(TAG, "Failed to add comment", e)
    }
}
