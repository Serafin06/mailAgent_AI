package pl.rafapp.service

import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.data.message.SystemMessage
import pl.rafapp.config.OllamaConfig

// Serwis LLM - LangChain4j zarządza komunikacją z Ollamą
class OllamaService(private val agent: EmailAgent) {

    suspend fun analyzeEmail(subject: String, content: String): String {
        return agent.analyzeEmail(subject, content)
    }
}