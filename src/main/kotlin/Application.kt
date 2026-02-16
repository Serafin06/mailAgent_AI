package pl.rafapp

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import kotlinx.serialization.json.Json
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import pl.rafapp.config.MailConfig
import pl.rafapp.config.OllamaConfig
import pl.rafapp.service.MailAnalysisService
import pl.rafapp.service.MailService
import pl.rafapp.service.OllamaService


fun Application.module() {
    // Konfiguracja
    val ollamaConfig = OllamaConfig.fromEnvironment(environment)
    val mailConfig = MailConfig.fromEnvironment(environment)

    // HTTP Client dla Ollamy
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    // Dependency Injection - ręczne (można zastąpić Koin/Kodein)
    val mailService = MailService(mailConfig)
    val ollamaService = OllamaService(httpClient, ollamaConfig)
    val analysisService = MailAnalysisService(mailService, ollamaService)

    // Thymeleaf
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

