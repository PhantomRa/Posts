package directMessages

import posts.Attachment

open class DirectMessages(
    open val fromId: Long,
    open val toId: Long,
    open val chatId: Long,
    val message: MutableList<Message> = mutableListOf(),
    val date: Long = 0,
    val attachments: Array<Attachment> = emptyArray(),
    val important: Boolean = false,
    val fwdMessages: List<String> = emptyList(),
    val conversationMessageId: Long = 0,
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(
            "Chat(",
            "fromId = $fromId ",
            "toId = $toId ",
            "chatId = $chatId ",
            "message = $message ",
            "date = $date ",
            "attachments = $attachments ",
            "important = $important ",
            "fwdMessages = $fwdMessages ",
            "conversationMessageId = $conversationMessageId)"
        )
        return sb.toString()
    }
}