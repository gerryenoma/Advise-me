@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.adviseme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adviseme.data.FeedRepository
import com.example.adviseme.model.Post
import kotlinx.coroutines.launch

@Composable
fun FeedScreen() {
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var newPostText by remember { mutableStateOf("") }

    /* Initial Firestore fetch */
    LaunchedEffect(Unit) { posts = FeedRepository.fetchPosts() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        /* ---------- New‑post input ---------- */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.AccountCircle, "Profile",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .border(1.dp, Color.Gray, CircleShape),
                    tint = Color.Black
                )

                Spacer(Modifier.width(8.dp))

                TextField(
                    value = newPostText,
                    onValueChange = { newPostText = it },
                    placeholder = { Text("What's on your mind?", color = Color.Black) },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp)),
                    maxLines = 3,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor            = Color.Transparent,
                        focusedTextColor          = Color.Black,
                        unfocusedTextColor        = Color.Black,
                        cursorColor               = Color.Black,
                        focusedIndicatorColor     = Color.Transparent,
                        unfocusedIndicatorColor   = Color.Transparent
                    )
                )

                IconButton(onClick = {
                    if (newPostText.isNotBlank()) {
                        scope.launch {
                            FeedRepository.addPost("You", newPostText)
                            newPostText = ""
                            posts = FeedRepository.fetchPosts()
                        }
                    }
                }) {
                    Icon(Icons.Default.Send, "Post", tint = Color.Black)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        /* ---------- Posts list ---------- */
        LazyColumn(Modifier.fillMaxSize()) {
            items(posts) { post ->
                var commentText by remember { mutableStateOf("") }
                var showBox by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .padding(bottom = 8.dp)
                ) {
                    /* Post header */
                    PostItem(post)

                    Spacer(Modifier.height(6.dp))

                    /* Comment & Like row */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /* Comment toggle */
                        Row(
                            modifier = Modifier
                                .clickable { showBox = !showBox },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Comment, "Comment", tint = Color.Gray)
                            Spacer(Modifier.width(4.dp))
                            Text("Comment", color = Color.Gray)
                        }

                        Spacer(Modifier.width(16.dp))

                        /* Like icon */
                        Row(
                            modifier = Modifier
                                .clickable { /* TODO: Like logic */ },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = Color.Gray
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Like", color = Color.Gray)
                        }
                    }

                    /* Comment box */
                    if (showBox) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                placeholder = { Text("Write a comment...", color = Color.Black) },
                                modifier = Modifier.weight(1f),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor          = Color.Transparent,
                                    focusedTextColor        = Color.Black,
                                    unfocusedTextColor      = Color.Black,
                                    cursorColor             = Color.Black,
                                    focusedIndicatorColor   = Color.Gray,
                                    unfocusedIndicatorColor = Color.LightGray
                                )
                            )
                            IconButton(onClick = {
                                /* Guard: non‑blank comment AND valid Firestore doc ID */
                                if (commentText.isNotBlank() && post.id.isNotBlank()) {
                                    scope.launch {
                                        FeedRepository.addComment(post.id, commentText)
                                        commentText = ""
                                        posts = FeedRepository.fetchPosts()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Send, "Send Comment", tint = Color.Black)
                            }
                        }
                    }

                    /* Comment list */
                    post.comments.forEach { comment ->
                        Column(modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)) {
                            Text(
                                text = post.userName, // Name atop the comment
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AccountCircle, "Commenter",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray)
                                        .border(1.dp, Color.Gray, CircleShape),
                                    tint = Color.Black
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(comment, color = Color.Black)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/* ---------- Small helper ---------- */
@Composable
private fun PostItem(post: Post) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Filled.AccountCircle, "User avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .border(1.dp, Color.Gray, CircleShape),
            tint = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                post.userName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(post.content, style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black))
        }
    }
}
