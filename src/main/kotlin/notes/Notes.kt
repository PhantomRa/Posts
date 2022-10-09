package notes

import java.time.LocalDateTime

open class Notes(
    open val noteId: Long,
    open val ownerId: Long,
    val title: String = "Empty title",
    val text: String = "Enter text here",
    val date: LocalDateTime = LocalDateTime.now(),
    var comments: MutableList<NoteComments> = mutableListOf(),
    val readComments: Int = 0,
    val viewUrl: String? = null,
    val privacyView: Int = 0,
    val canComment: Boolean = true,
    val textWiki: String? = null,
    var isDeleted: Boolean = false,
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(
            "Note(",
            "noteId = $noteId ",
            "ownerId = $ownerId ",
            "title = $title ",
            "text = $text ",
            "date = $date ",
            "comments = $comments ",
            "readComments = $readComments ",
            "viewUrl = $viewUrl ",
            "privacyView = $privacyView ",
            "canComment = $canComment ",
            "textWiki = $textWiki ",
            "isDeleted = $isDeleted"
        )
        return sb.toString()
    }
}