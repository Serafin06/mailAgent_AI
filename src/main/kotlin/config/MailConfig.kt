package pl.rafapp.config

import io.ktor.server.application.*

// Konfiguracja IMAP - centralizacja credentials i parametrów
data class MailConfig(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val folder: String
) {
    companion object {
        fun fromEnvironment(environment: ApplicationEnvironment): MailConfig {
            return MailConfig(
                host = environment.config.property("mail.host").getString(),
                port = environment.config.property("mail.port").getString().toInt(),
                username = environment.config.property("mail.username").getString(),
                password = environment.config.property("mail.password").getString(),
                folder = environment.config.property("mail.folder").getString()
            )
        }
    }
}