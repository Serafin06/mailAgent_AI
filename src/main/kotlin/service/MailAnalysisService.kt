package pl.rafapp.service

import pl.rafapp.model.Email

// Orkiestrator analizy maili - odpowiedzialność: koordynacja workflow
class MailAnalysisService(
    private val mailService: MailService,
    private val ollamaService: OllamaService
) {

    suspend fun analyzeUnreadEmails(limit: Int = 10): List<Pair<Email, String>> {
        val emails = mailService.fetchUnreadEmails(limit)

        return emails.map { email ->
            val analysis = ollamaService.analyzeEmail(
                subject = email.subject,
                content = email.content
            )
            email to analysis
        }
    }

    suspend fun processEmail(email: Email): String {
        return ollamaService.analyzeEmail(email.subject, email.content)
    }
}