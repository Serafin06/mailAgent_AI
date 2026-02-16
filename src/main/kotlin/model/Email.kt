package pl.rafapp.model

import kotlinx.serialization.Serializable
import java.util.Date

// Model reprezentujący email z serwera IMAP
data class Email(
    val messageId: String,
    val from: String,
    val subject: String,
    val content: String,
    val receivedDate: Date,
    val isRead: Boolean
)