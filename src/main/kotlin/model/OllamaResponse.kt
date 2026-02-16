package pl.rafapp.model

import kotlinx.serialization.Serializable

// Response z API Ollamy - mapowanie odpowiedzi JSON
@Serializable
data class OllamaResponse(
    val model: String,
    val response: String,
    val done: Boolean
)