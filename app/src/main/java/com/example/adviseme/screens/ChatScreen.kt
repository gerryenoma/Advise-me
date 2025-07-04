@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.adviseme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adviseme.data.ChatRepository
import com.example.adviseme.model.Conversation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(nav: NavController) {
    val myUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val conversations by ChatRepository.conversationsFlow().collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(conversations) { conv ->
            ConversationItem(conv, myUid) {
                val otherUid = conv.participants.first { it != myUid }
                nav.navigate("chat/${conv.id}/$otherUid")
            }
        }
    }
}

@Composable
private fun ConversationItem(
    conv: Conversation,
    myUid: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(Modifier.width(12.dp))

            Column {
                val otherUid = conv.participants.first { it != myUid }
                Text(otherUid, style = MaterialTheme.typography.bodyLarge)
                Text(
                    conv.lastMessage,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    maxLines = 1
                )
            }
        }
    }
}
