package Notes

import java.time.LocalDateTime

data class NoteComments(
    val commentId: Int = 1,
    val noteId: Int,
    val ownerId: Int,
    val replyTo: Int? = null,
    val date: LocalDateTime = LocalDateTime.now(),
    val message: String,
)
