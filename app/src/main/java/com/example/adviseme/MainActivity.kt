package com.example.adviseme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.adviseme.navigation.AppNavigation
import com.example.adviseme.ui.theme.AdviseMeTheme
import com.google.firebase.FirebaseApp          // ← add this import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔑 Initialize the Firebase SDK (runs once when the activity starts)
        FirebaseApp.initializeApp(this)

        setContent {
            AdviseMeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}
