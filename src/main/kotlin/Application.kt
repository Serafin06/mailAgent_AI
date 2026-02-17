package pl.rafapp

import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.service.AiServices
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import pl.rafapp.config.MailConfig
import pl.rafapp.config.OllamaConfig
import pl.rafapp.service.EmailAgent
import pl.rafapp.service.MailAnalysisService
import pl.rafapp.service.MailService
import pl.rafapp.service.OllamaService

fun Application.module() {
    val ollamaConfig = OllamaConfig.fromEnvironment(environment)
    val mailConfig = MailConfig.fromEnvironment(environment)

    // Budowanie modelu LangChain4j
    val chatModel = OllamaChatModel.builder()
        .baseUrl(ollamaConfig.url)
        .modelName(ollamaConfig.model)
        .temperature(0.7)
        .build()

    // Agent z interfejsem
    val emailAgent = AiServices.builder(EmailAgent::class.java)
        .chatLanguageModel(chatModel)
        .build()

    // Serwisy
    val mailService = MailService(mailConfig)
    val ollamaService = OllamaService(emailAgent)
    val analysisService = MailAnalysisService(mailService, ollamaService)

    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }

    routing {
        get("/") {
            call.respondText("Email AI Agent działa!")
        }
        get("/analyze") {
            val results = analysisService.analyzeUnreadEmails(5)
            call.respond(ThymeleafContent("analysis", mapOf("results" to results)))
        }
    }
}