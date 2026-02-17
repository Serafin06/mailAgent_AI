package pl.rafapp.service

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import dev.langchain4j.service.AiServices

// Interfejs agenta - LangChain4j generuje implementację automatycznie
interface EmailAgent {

    @SystemMessage("Jesteś asystentem analizującym emaile firmowe. Odpowiadasz w JSON.")
    @UserMessage("Przeanalizuj email. Temat: {{subject}} Treść: {{content}}")
    fun analyzeEmail(subject: String, content: String): String

    @SystemMessage("Jesteś asystentem piszącym odpowiedzi na emaile.")
    @UserMessage("Napisz profesjonalną odpowiedź na: {{email}}")
    fun draftReply(email: String): String
}
