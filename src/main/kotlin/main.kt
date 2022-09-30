import Notes.NoteComments
import Notes.Notes
import Posts.IdNotFoundException
import Posts.Post
import Posts.PostNotFoundException

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

object NoteService {
    private var notes: MutableList<Notes> = mutableListOf()
    private var deletedComments: MutableList<NoteComments> = mutableListOf()
    private var nextNoteId = 1
    private var nextCommentId = 1

    fun clear() {
        notes = mutableListOf()
    }

    fun add(_note: Notes): Notes? {
        if (!notes.contains(_note)) {
            val newNote = _note.copy(id = nextNoteId)
            notes.add(newNote)
            nextNoteId++
            return notes.last()
        }
        return null
    }

    fun createComment(_comment: NoteComments): NoteComments? {
        notes.forEach {
            if (it.id == _comment.noteId && it.ownerId == _comment.ownerId) {
                val comment = NoteComments(
                    nextCommentId,
                    it.id,
                    it.ownerId,
                    _comment.replyTo,
                    _comment.date,
                    _comment.message,
                )
                nextCommentId++
                it.comments?.add(comment)
                return it.comments?.last()
            }
        }
        return null
    }

    fun delete(noteId: Int): Boolean {
        for ((index, note) in notes.withIndex()) {
            if (note.id == noteId) {
                notes.removeAt(index)
                return true
            }
        }
        return false
    }

    fun deleteComment(commentId: Int, ownerId: Int): Boolean {
        notes.forEach { note ->
            if (note.ownerId == ownerId) {
                note.comments?.forEach { it ->
                    if (it.commentId == commentId) {
                        deletedComments.add(note.comments!![commentId])
                        note.comments!!.removeAt(commentId - 1)
                        return true
                    }
                }
            }
        }
        return false
    }

    fun edit(_note: Notes): Boolean {
        notes.forEach {
            if (it.id == _note.id && it.ownerId == _note.ownerId) {
                val editedNote = it.copy(
                    id = it.id,
                    ownerId = it.ownerId,
                    title = _note.title,
                    text = _note.text,
                    date = _note.date,
                    comments = _note.comments,
                    readComments = _note.readComments,
                    viewUrl = _note.viewUrl,
                    privacyView = _note.privacyView,
                    canComment = _note.canComment,
                    textWiki = _note.textWiki
                )
                notes[notes.indexOf(it)] = editedNote
                return true
            }
        }
        return false
    }

    fun editComment(commentId: Int, noteId: Int, ownerId: Int, _message: String): Boolean {
        notes.forEach { note ->
            if (note.id == noteId && note.ownerId == ownerId) {
                note.comments?.forEach {
                    if (it.commentId == commentId) {
                        note.comments!![note.comments!!.indexOf(it)] = it.copy(date = note.date, message = _message)
                        return true
                    }
                }
            }
        }
        return false
    }

    fun get(noteIds: IntRange, userId: Int, sort: Int = 0): MutableList<Notes> {
        val list: MutableList<Notes> = mutableListOf()
        noteIds.forEach {
            for (note in notes) if (note.id.compareTo(it) == 0 && note.ownerId == userId) list.add(note)
        }
        return if (sort == 1) list
        else {
            list.reverse()
            list
        }
    }

    fun getById(
        noteId: Int,
        ownerId: Int,
        needWiki: Boolean = false,
    ): Notes? = notes.find { it.id == noteId && it.ownerId == ownerId }

    fun getComments(noteId: Int, ownerId: Int, sort: Int = 1): MutableList<NoteComments>? {
        var list: MutableList<NoteComments>? = mutableListOf()
        notes.forEach {
            if (it.id == noteId && it.ownerId == ownerId) {
                list = it.comments
            }
        }
        if (sort == 1) return list
        else if (sort == 0) {
            list!!.reverse()
            return list
        }
        return null
    }

    fun getFriendsNotes(ownerId: Int): MutableList<Notes> = notes.filter { it.ownerId == ownerId }.toMutableList()

    fun restoreComment(commentId: Int, noteId: Int, ownerId: Int): Boolean {
        deletedComments.forEach { comment ->
            if (comment.commentId == commentId && comment.noteId == noteId && comment.ownerId == ownerId) {
                notes.forEach { note ->
                    if (note.id == noteId && note.ownerId == ownerId) {
                        note.comments?.add(commentId - 1, comment)
                        return true
                    }
                }
            }
        }
        return false
    }
}

fun main() {
    val service = NoteService
    val note1 = Notes()
    service.add(note1)
    service.createComment(NoteComments(0, 1,1, message = "message"))
    service.createComment(NoteComments(0, 1,1, message = "message2"))
    println(service.getComments(1, note1.ownerId))

    println(service.editComment(2, 1, 1, "UPDATED!"))
    println(service.getComments(1, note1.ownerId))
}
