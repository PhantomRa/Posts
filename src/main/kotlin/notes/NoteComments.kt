package notes

import java.time.LocalDateTime

data class NoteComments(
    override val noteId: Long,
    override val ownerId: Long,
    val id: Long,
    val replyTo: Int? = null,
    val commentDate: LocalDateTime = LocalDateTime.now(),
    val message: String,
    var commentIsDeleted: Boolean = false,
) : Notes(noteId, ownerId)
