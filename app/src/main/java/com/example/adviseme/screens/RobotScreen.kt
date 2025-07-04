@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.adviseme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adviseme.R
import com.example.adviseme.api.ChatViewModel

@Composable
fun RobotScreen(viewModel: ChatViewModel = remember { ChatViewModel() }) {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    val messages = viewModel.messages
    val isLoading by viewModel.isLoading
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            if (messages.isEmpty()) {
                Text(
                    "Advise me?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            } else {
                messages.forEach { msg ->
                    val isUser = msg.role == "user"
                    MessageBubble(message = msg.content, isUser = isUser)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                                .widthIn(max = 200.dp)
                        ) {
                            Text(text = "Typing...", color = Color.Gray)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)),
                placeholder = {
                    Text("Type a message...")
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFF0F0F0),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Blue,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                )
            )

            IconButton(
                onClick = {
                    if (input.text.isNotBlank()) {
                        viewModel.sendMessage(input.text)
                        input = TextFieldValue("")
                        focusManager.clearFocus(force = true)
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: String, isUser: Boolean) {
    val userBlue = Color(0xFF1976D2) // ✅ lighter blue for user bubble
    val robotBlue = Color(0xFFD0E8FF)

    val backgroundColor = if (isUser) userBlue else robotBlue
    val textColor = Color.Black
    val borderColor = if (isUser) userBlue else Color.Black.copy(alpha = 0.1f)

    val bubbleShape = if (isUser) {
        RoundedCornerShape(12.dp, 0.dp, 12.dp, 12.dp)
    } else {
        RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            Image(
                painter = painterResource(id = R.drawable.robot),
                contentDescription = "Robot",
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .background(backgroundColor, bubbleShape)
                .border(1.dp, borderColor, bubbleShape)
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(text = message, color = textColor)
        }

        if (isUser) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "User",
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
