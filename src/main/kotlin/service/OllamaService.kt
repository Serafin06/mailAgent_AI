package pl.rafapp.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import pl.rafapp.config.OllamaConfig
import pl.rafapp.model.OllamaRequest
import pl.rafapp.model.OllamaResponse

// Serwis do komunikacji z Ollamą - odpowiedzialność: zapytania do LLM
class OllamaService(
    private val client: HttpClient,
    private val config: OllamaConfig
) {

    suspend fun generateResponse(prompt: String): String {
        val request = OllamaRequest(
            model = config.model,
            prompt = prompt,
            stream = false
        )

        val response: OllamaResponse = client.post("${config.url}/api/generate") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        return response.response
    }

    suspend fun analyzeEmail(subject: String, content: String): String {
        val prompt = """
            Przeanalizuj poniższy email i odpowiedz w formacie JSON:
            
            Temat: $subject
            Treść: $content
            
            Zwróć JSON z polami:
            - kategoria: (pilne/ważne/informacyjne/spam)
            - priorytet: (wysoki/średni/niski)
            - wymagana_akcja: (tak/nie)
            - podsumowanie: (krótkie podsumowanie 1-2 zdania)
            - sugerowana_odpowiedz: (szkic odpowiedzi jeśli wymagana akcja)
        """.trimIndent()

        return generateResponse(prompt)
    }
}