import directMessages.DirectMessages
import directMessages.Message
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatKtTest {
    private val service = ChatService

    @Before
    fun clear() {
        service.clear()
    }

    @Test
    fun add() {
        val id = service.add(DirectMessages(1, 2, 1))
        assertEquals(1, id)
    }

    @Test
    fun delete() {
        service.add(DirectMessages(1, 2, 1))
        service.add(DirectMessages(1, 2, 2))
        service.delete(1)
        assertEquals(1, service.read().size)
    }

    @Test
    fun read() {
        for (i in 1..6L) {
            service.add(DirectMessages(1, 2, i))
        }
        assertEquals(6, service.read().size)
    }

    @Test
    fun getById() {
        val list: MutableList<DirectMessages> = mutableListOf()
        for (i in 1..6L) {
            val chat = DirectMessages(1, 2, i)
            service.add(chat)
            list.add(chat)
        }
        assertEquals(list[1], service.getById(2))
    }

    @Test
    fun addMessage() {
        val message = Message(1, 2, 1, 1, "test")
        service.addMessage(1, 2, 1, message)
        assertEquals(message, service.getMessages(1, 1)[0])
    }

    @Test
    fun deleteMessage() {
        var message: Message
        for (i in 1..3L) {
            message = Message(1, 2, 1, i, "test")
            service.addMessage(message.fromId, message.toId, message.chatId, message)
        }
        assertEquals(3, service.getMessages(1, 1).size)
        service.deleteMessage(1, 2, 1, 1)
        assertEquals(2, service.getMessages(1, 1).size)
    }

    @Test
    fun editMessage() {
        val message = Message(1, 2, 1, 1, "test")
        service.addMessage(1, 2, 1, message)
        val editMessage = Message(1, 2, 1, 1, "UPDATED", true)
        service.editMessage(editMessage)
        assertEquals(editMessage, service.getMessages(1, 1)[0])
    }

    @Test
    fun getMessages() {
        for (i in 1..6L) {
            val message = Message(1, i + 1, 1, i, "message $i")
            service.addMessage(1, i + 1, 1, message)
        }
        assertEquals(6, service.getMessages(1, 1).size)
    }

    @Test
    fun getUnreadChatsCount() {
        for (i in 1..6L) {
            val message = Message(1, i + 1, i, i, "message $i")
            service.addMessage(1, i + 1, i, message)
        }
        service.getMessages(1, 1)
        assertEquals(5, service.getUnreadChatsCount(1))
    }
}