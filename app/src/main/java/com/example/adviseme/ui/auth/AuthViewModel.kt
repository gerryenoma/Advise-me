package com.example.adviseme.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.OAuthProvider

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    val isSignedIn: Boolean
        get() = auth.currentUser != null && !auth.currentUser!!.isAnonymous

    fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { t ->
                if (t.isSuccessful) onResult(true, null)
                else onResult(false, t.exception?.message ?: "Unknown error")
            }
    }

    fun signUpWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { t ->
                if (t.isSuccessful) onResult(true, null)
                else onResult(false, t.exception?.message ?: "Sign-up failed")
            }
    }

    fun signInAnonymously(onComplete: () -> Unit) {
        auth.signInAnonymously().addOnSuccessListener { onComplete() }
    }

    // 🟦 Facebook sign-up (stub)
    fun signUpWithFacebook(onResult: (Boolean, String?) -> Unit) {
        // This should be called with an accessToken from Facebook SDK
        val accessToken = /* TODO: Get from Facebook SDK */ null
        if (accessToken == null) {
            onResult(false, "Facebook login failed")
            return
        }

        val credential = FacebookAuthProvider.getCredential(accessToken)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message ?: "Facebook sign-up failed")
            }
    }

    // 🔴 Google sign-up (stub)
    fun signUpWithGoogle(idToken: String?, onResult: (Boolean, String?) -> Unit) {
        if (idToken == null) {
            onResult(false, "Google ID Token missing")
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.message ?: "Google sign-up failed")
            }
    }

    // 🍏 Apple ID sign-up (stub)
    fun signUpWithApple(activity: Activity, onResult: (Boolean, String?) -> Unit) {
        val provider = OAuthProvider.newBuilder("apple.com")
            .addCustomParameter("locale", "en")
            .build()

        auth
            .startActivityForSignInWithProvider(activity, provider)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message ?: "Apple sign-up failed") }
    }
}
