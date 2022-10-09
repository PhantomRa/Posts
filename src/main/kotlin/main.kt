import directMessages.DirectMessages
import directMessages.Message
import notes.IdAlreadyExistsException
import notes.InvalidIdException
import notes.NoteComments
import notes.Notes
import posts.IdNotFoundException
import posts.Post
import posts.PostNotFoundException

object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Post.Comments>()
    private var reportedComments = emptyArray<Post.Comments>()
    private var nextId = 1

    fun clear() {
        posts = emptyArray()
    }

    fun add(post: Post): Post {
        posts += if (post.likes.count < 0)
            post.copy(id = nextId, likes = Post.Likes(0,
                post.likes.userLikes,
                post.likes.canLike,
                post.likes.canPublish))
        else post.copy(id = nextId)

        nextId++
        return posts.last()
    }

    fun update(_post: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (_post.id == post.id) {
                posts[index] = post.copy(
                    text = _post.text,
                    likes = _post.likes,
                    views = _post.views,
                    canPin = _post.canPin,
                    canDelete = _post.canDelete,
                    canEdit = _post.canEdit,
                    markedAsAds = _post.markedAsAds,
                    isFavorite = _post.isFavorite)
                return true
            }
        }
        return false
    }

    fun likeById(id: Int): Post? {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                posts[index] = post.copy(likes = Post.Likes(post.likes.count + 1,
                    true,
                    post.likes.canLike,
                    post.likes.canPublish), views = Post.Views(post.views.count + 1))
                return posts[index]
            }
        }
        return null
    }

    fun createComment(postId: Int, comment: Post.Comments): Post.Comments {
        for (post in posts) {
            if (post.id == postId) {
                comments += comment
                return comments.last()
            }
        }
        throw PostNotFoundException("No post with id = $postId")
    }

    fun reportComment(ownerId: Int, commentId: Int, reason: Int): Boolean {
        if (commentId < 0) throw IdNotFoundException("Id not found")
        if (ownerId < 0) throw IdNotFoundException("Id not found")
        for (post in posts) {
            if (post.ownerId == ownerId && post.comments?.id == commentId) {
                reportedComments += post.comments!!
                return true
            }
        }
        throw PostNotFoundException("Posts.Post not found")
    }

    fun printPosts() {
        for ((index, _) in posts.withIndex()) {
            println(posts[posts.size - 1 - index])
        }
    }
}

interface CrudService<E> {
    fun clear()

    fun add(entity: E): Long

    fun delete(id: Long)

    fun edit(entity: E)

    fun read(): List<E>

    fun getById(id: Long): E

    fun restore(id: Long)
}

private var notes: MutableList<Notes> = mutableListOf()
private var comments: MutableList<NoteComments> = mutableListOf()
private var chats: MutableList<DirectMessages> = mutableListOf()

object NoteService : CrudService<Notes> {
    override fun clear() {
        notes = mutableListOf()
    }

    override fun add(entity: Notes): Long {
        if (entity.noteId < 1) throw InvalidIdException("Неверный ID")
        if (notes.find { it.noteId == entity.noteId } == null) {
            notes.add(entity)
            return entity.noteId
        } else {
            throw IdAlreadyExistsException("Такой ID уже существует")
        }
    }

    override fun delete(id: Long) {
        val note = notes.find { it.noteId == id }
        note?.isDeleted = true
    }

    override fun edit(entity: Notes) {
        for ((index, note) in notes.withIndex())
            if (entity.noteId == note.noteId && entity.ownerId == note.ownerId) notes[index] = entity
    }

    override fun read(): List<Notes> {
        return notes.filter { !it.isDeleted }
    }

    override fun getById(id: Long): Notes {
        return notes.find { it.noteId == id } ?: throw IdNotFoundException("Заметка с таким ID не найдена")
    }

    override fun restore(id: Long) {
        TODO("Not yet implemented")
    }
}

object NoteCommentService : CrudService<NoteComments> {
    override fun clear() {
        comments = mutableListOf()
    }

    override fun add(entity: NoteComments): Long {
        if (entity.id < 1) throw InvalidIdException("Неверный ID")
        if (comments.find { it.id == entity.id } == null) {
            comments.add(entity)
            return entity.id
        } else {
            throw IdAlreadyExistsException("Такой ID уже существует")
        }
    }

    override fun delete(id: Long) {
        val comment = comments.find { it.id == id }
        comment?.isDeleted = true
    }

    override fun edit(entity: NoteComments) {
        for ((index, comment) in comments.withIndex())
            if (comment.id == entity.id && comment.ownerId == entity.ownerId) comments[index] = entity
    }

    override fun read(): List<NoteComments> {
        return comments.filter { !it.isDeleted }
    }

    override fun getById(id: Long): NoteComments {
        return comments.find { it.id == id } ?: throw IdNotFoundException("Заметка с таким ID не найдена")
    }

    override fun restore(id: Long) {
        val deletedComment = comments.find { it.id == id } ?: throw IdNotFoundException("ID не найден")
        deletedComment.isDeleted = false
        for ((index, comment) in comments.withIndex()) {
            if (comment.id == id) comments[index] = deletedComment
        }
    }
}

object ChatService : CrudService<DirectMessages> {
    override fun clear() {
        chats = mutableListOf()
    }

    override fun add(entity: DirectMessages): Long {
        if (entity.chatId < 1) throw InvalidIdException("Неверный ID")
        if (chats.find { it.chatId == entity.chatId } == null) {
            chats.add(entity)
            return entity.chatId
        } else {
            throw IdAlreadyExistsException("Такой ID уже существует")
        }
    }

    override fun delete(id: Long) {
        for ((index, message) in chats.withIndex()) {
            if (message.chatId == id) chats.removeAt(index)
        }
    }

    //    Not used
    override fun edit(entity: DirectMessages) {}

    override fun read(): List<DirectMessages> = chats.toList()

    override fun getById(id: Long): DirectMessages =
        chats.find { it.chatId == id } ?: throw IdNotFoundException("Чат с таким ID не найден")

    //    Not used
    override fun restore(id: Long) {}

    fun addMessage(userId: Long, toId: Long, chatId: Long, message: Message) {
        val chat = chats.find {
            it.fromId == userId && it.chatId == chatId
        }
        if (chat != null) chat.message.add(message)
        else {
            val entity = DirectMessages(userId, toId, chatId)
            entity.message.add(message)
            add(entity)
        }
    }

    fun deleteMessage(userId: Long, toId: Long, chatId: Long, messageId: Long) {
        val chat = chats.find {
            it.fromId == userId && it.toId == toId && it.chatId == chatId
        }

        if (chat?.message!!.count() > 1)
            chat.message.removeIf { it.messageId == messageId }
        else {
            chats.remove(chat)
        }
    }

    fun editMessage(entity: Message) {
        val chat = chats.find { it.fromId == entity.fromId && it.toId == entity.toId && it.chatId == entity.chatId }
        chat?.message?.forEach { message ->
            if (message.messageId == entity.messageId) {
                message.text = entity.text
                message.messageDate = entity.messageDate
            }
        }
    }

    fun getMessages(userId: Long, chatId: Long): List<Message> {
        val list = chats.find {
            it.fromId == userId && it.chatId == chatId
        }?.message!!.toList()
        list.onEach { it.readState = true }
        return list
    }

    fun getUnreadChatsCount(userId: Long): Int {
        var count = 0
        chats.forEach { chat ->
            if (chat.fromId == userId && chat.message.count { !it.readState } > 0) count++
        }
        return count
    }
}

fun main() {
    val service = ChatService

    for (i in 1..3L) {
        service.addMessage(99, 90, i, Message(99, 90, i, i, "message $i"))
    }

    println(service.read())
    println(service.getMessages(99, 1))
    println(service.getUnreadChatsCount(99))
}
