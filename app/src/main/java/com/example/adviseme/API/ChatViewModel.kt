package com.example.adviseme.api

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adviseme.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ChatViewModel : ViewModel() {

    val messages = mutableStateListOf<ChatMessage>()
    val isLoading = mutableStateOf(false)

    fun sendMessage(userInput: String) {
        if (userInput.isBlank()) return

        messages.add(ChatMessage("user", userInput))
        isLoading.value = true

        viewModelScope.launch {
            try {
                val request = CohereChatRequest(message = userInput)

                // Make the API call in IO dispatcher
                val reply = withContext(Dispatchers.IO) {
                    CohereClient.api.chat(request).text
                }

                messages.add(ChatMessage("assistant", reply))

            } catch (e: HttpException) {
                messages.add(ChatMessage("assistant", "❌ HTTP ${e.code()}: ${e.message()}"))
            } catch (e: IOException) {
                messages.add(ChatMessage("assistant", "🌐 Network error: ${e.localizedMessage}"))
            } catch (e: Exception) {
                messages.add(
                    ChatMessage(
                        "assistant",
                        "💥 Unexpected error: ${e.message ?: e::class.java.simpleName}"
                    )
                )
            } finally {
                isLoading.value = false
            }
        }
    }
}
