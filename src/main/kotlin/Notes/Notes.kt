package Notes

import java.time.LocalDateTime

data class Notes(
    val id: Int = 1,
    val ownerId: Int = 1,
    val title: String = "Empty title",
    val text: String = "Enter text here",
    val date: LocalDateTime = LocalDateTime.now(),
    var comments: MutableList<NoteComments>? = mutableListOf(),
    val readComments: Int = 0,
    val viewUrl: String? = null,
    val privacyView: Int = 0,
    val canComment: Boolean = true,
    val textWiki: String? = null,
)