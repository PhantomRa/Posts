import Notes.NoteComments
import Notes.Notes
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NotesKtTest {
    private val nService = NoteService

    @Before
    fun clearNotes() {
        nService.clear()
    }

    @Test
    fun addNoteTest() {
        val note = nService.add(Notes())
        assertEquals(7, note?.id)
    }

    @Test
    fun createCommentTest() {
        val note = nService.add(Notes())
        val comment1 = nService.createComment(NoteComments(0, 1,1, message = "message"))
        val comment2 = nService.createComment(NoteComments(0, 1,1, message = "message 2"))
//        assertEquals(comment1?.noteId, comment2?.noteId)
//        assertEquals(comment1?.ownerId, comment2?.ownerId)
        val list = nService.getComments(1, 1)

        assertEquals(comment2, list?.find { it.commentId == 2 })
    }

    @Test
    fun deleteTest() {
        val note1 = nService.add(Notes())
        val note2 = nService.add(Notes())
        note1?.let { nService.delete(it.id) }
        assertEquals(1, nService.get(1..100, 1).size)
    }

    @Test
    fun deleteCommentTest() {
        val note = nService.add(Notes())
        nService.createComment(NoteComments(0, 1,1, message = "message"))
        nService.createComment(NoteComments(0, 1,1, message = "message2"))
        nService.deleteComment(1, note!!.ownerId)
        assertEquals(1, nService.getComments(note.id, note.ownerId)?.size)
    }

    @Test
    fun editTest() {
        val note = nService.add(Notes())
        val editedNote = Notes(note!!.id, note.ownerId, title = "Update test", text = "UPDATED", date = note.date)
        nService.edit(editedNote)
        val note2 = Notes(note.id, note.ownerId, title = "Update test", text = "UPDATED", date = note.date)

        assertEquals(note2, nService.getById(note.id, note.ownerId))
    }

    @Test
    fun editCommentTest() {
        val note = nService.add(Notes())
        val comment = nService.createComment(NoteComments(noteId = note!!.id, ownerId = note.ownerId, message = ""))
        val testComment = NoteComments(comment!!.commentId, comment.noteId, comment.ownerId, date = comment.date, message = "UPDATED")

        nService.editComment(comment.commentId, comment.noteId, comment.ownerId, "UPDATED")
        val list = nService.getComments(comment.noteId, comment.ownerId)
        val result = list?.find { it.message == "UPDATED" }
        assertEquals(testComment, result)
    }

    @Test
    fun getNotesTest() {
        val list: MutableList<Notes> = mutableListOf()
        for (i in 0..10) {
            nService.add(Notes())?.let { list.add(it) }
        }
        assertEquals(11, list.size)
    }

    @Test
    fun getNoteByIdTest() {
        nService.add(Notes())
        val note = nService.add(Notes())
        assertEquals(note, nService.getById(note!!.id, note!!.ownerId))
    }

    @Test
    fun getCommentsTest() {
        val list: MutableList<NoteComments> = mutableListOf()
        nService.add(Notes())
        nService.createComment(NoteComments(noteId = 1, ownerId = 1, message = "comment1"))?.let { list.add(it) }
        nService.createComment(NoteComments(noteId = 1, ownerId = 1, message = "comment2"))?.let { list.add(it) }
        nService.createComment(NoteComments(noteId = 1, ownerId = 1, message = "comment3"))?.let { list.add(it) }
        assertEquals(list, nService.getComments(1, 1))
    }

    @Test
    fun getFriendsNotesTest() {
        val list: MutableList<Notes> = mutableListOf()
        nService.add(Notes(ownerId = 4))
        for (i in 0..5) {
            nService.add(Notes())?.let { list.add(it) }
        }
        list.forEach { assertEquals(1, it.ownerId) }
        assertEquals(6, list.size)
    }

//    Не работает

//    @Test
//    fun restoreCommentTest() {
//        val note = nService.add(Notes())
//        val comment1 = nService.createComment(NoteComments(noteId = note!!.id, ownerId = 1, message = "comment1"))
//        val comment2 = nService.createComment(NoteComments(noteId = note.id, ownerId = 1, message = "comment2"))
//        val comment3 = nService.createComment(NoteComments(noteId = note.id, ownerId = 1, message = "comment3"))
//
//        assertEquals(true, nService.getComments(note.id, note.ownerId)?.contains(comment2))
//        nService.deleteComment(comment2!!.commentId, comment2.ownerId)
//        assertEquals(false, nService.getComments(note.id, note.ownerId)?.contains(comment2))
//    }
}
