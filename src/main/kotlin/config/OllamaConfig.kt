package pl.rafapp.config

import io.ktor.server.application.*

// Konfiguracja połączenia z Ollamą - centralizacja ustawień
data class OllamaConfig(
    val url: String,
    val model: String
) {
    companion object {
        fun fromEnvironment(environment: ApplicationEnvironment): OllamaConfig {
            return OllamaConfig(
                url = environment.config.property("ollama.url").getString(),
                model = environment.config.property("ollama.model").getString()
            )
        }
    }
}