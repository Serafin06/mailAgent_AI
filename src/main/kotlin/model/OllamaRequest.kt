package pl.rafapp.model

import kotlinx.serialization.Serializable

// Request do API Ollamy - enkapsulacja parametrów zapytania
@Serializable
data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false,
    val temperature: Double = 0.7
)