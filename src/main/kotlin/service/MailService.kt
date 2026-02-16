package pl.rafapp.service

import jakarta.mail.*
import jakarta.mail.internet.MimeMultipart
import jakarta.mail.search.FlagTerm
import pl.rafapp.config.MailConfig
import pl.rafapp.model.Email
import java.util.Properties

// Serwis do pobierania maili przez IMAP - odpowiedzialność: komunikacja z serwerem pocztowym
class MailService(private val config: MailConfig) {

    private fun createSession(): Session {
        val properties = Properties().apply {
            put("mail.store.protocol", "imaps")
            put("mail.imaps.host", config.host)
            put("mail.imaps.port", config.port)
            put("mail.imaps.ssl.enable", "true")
        }

        return Session.getInstance(properties)
    }

    fun fetchUnreadEmails(limit: Int = 10): List<Email> {
        val session = createSession()
        val store = session.getStore("imaps")

        return try {
            store.connect(config.host, config.username, config.password)
            val folder = store.getFolder(config.folder)
            folder.open(Folder.READ_ONLY)

            val messages = folder.search(FlagTerm(Flags(Flags.Flag.SEEN), false))
                .take(limit)

            messages.map { message ->
                Email(
                    messageId = message.getHeader("Message-ID")?.firstOrNull() ?: "",
                    from = message.from.firstOrNull()?.toString() ?: "Unknown",
                    subject = message.subject ?: "No Subject",
                    content = extractTextContent(message),
                    receivedDate = message.receivedDate,
                    isRead = message.flags.contains(Flags.Flag.SEEN)
                )
            }
        } finally {
            store.close()
        }
    }

    private fun extractTextContent(message: Message): String {
        return when (val content = message.content) {
            is String -> content
            is MimeMultipart -> {
                (0 until content.count)
                    .mapNotNull { content.getBodyPart(it) }
                    .filter { it.isMimeType("text/plain") }
                    .joinToString("\n") { it.content.toString() }
            }
            else -> ""
        }
    }
}