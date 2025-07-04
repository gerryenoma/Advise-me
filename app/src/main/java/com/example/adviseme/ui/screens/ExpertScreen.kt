package com.example.adviseme.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExpertScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Expert Screen", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewExpertScreen() {
    ExpertScreen()
}
