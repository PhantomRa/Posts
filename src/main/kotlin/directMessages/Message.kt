package directMessages

data class Message(
    override var fromId: Long,
    override var toId: Long,
    override var chatId: Long,
    val messageId: Long,
    var text: String,
    var readState: Boolean = false,
    var messageDate: Long = 0,
): DirectMessages(fromId, toId, chatId)