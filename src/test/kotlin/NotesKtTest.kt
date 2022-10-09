import notes.NoteComments
import notes.Notes
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NotesKtTest {
    private val nService = NoteService
    private val cService = NoteCommentService

    @Before
    fun clearNotes() {
        nService.clear()
    }

    @Before
    fun clearComments() {
        cService.clear()
    }

    @Test
    fun addNoteTest() {
        val noteId = nService.add(Notes(1, 1))
        assertEquals(1, noteId)
    }

    @Test
    fun createCommentTest() {
        val noteId = nService.add(Notes(1, 1))
        val comment1 = cService.add(NoteComments(noteId, noteId, 1, message = "message"))
        val comment2 = cService.add(NoteComments(noteId, noteId, 2, message = "message2"))

        assertEquals(1, comment1)
        assertEquals(2, comment2)
        assertEquals(2, cService.read().size)
    }

    @Test
    fun deleteTest() {
        val noteId1 = nService.add(Notes(3, 3))
        val noteId2 = nService.add(Notes(4, 4))
        nService.delete(noteId1)
        assertEquals(1, nService.read().size)
    }

    @Test
    fun deleteCommentTest() {
        val noteId = nService.add(Notes(5, 5))
        val comment1Id = cService.add(NoteComments(noteId, noteId, 3, message = "message"))
        val comment2Id = cService.add(NoteComments(noteId, noteId, 4, message = "message2"))
        cService.delete(comment1Id)
        assertEquals(1, cService.read().size)
    }

    @Test
    fun editTest() {
        val note = Notes(1, 1)
        val noteId = nService.add(note)
        val editedNote = Notes(noteId, noteId, title = "Update test", text = "UPDATED")
        nService.edit(editedNote)

        assertEquals("UPDATED", nService.getById(noteId).text)
    }

    @Test
    fun editCommentTest() {
        val note = Notes(6, 6)
        val noteId = nService.add(note)
        val commentId = cService.add(NoteComments(noteId, noteId, 5, message = "message"))
        val comment = NoteComments(noteId, noteId, commentId, commentDate = note.date, message = "UPDATED")
        cService.edit(comment)

        assertEquals(comment, cService.read()[0])
    }

    @Test
    fun getNotesTest() {
        for (i in 7..10L) {
            nService.add(Notes(i, i))
        }
        assertEquals(4, nService.read().size)
    }

    @Test
    fun getNoteByIdTest() {
        val note = Notes(11, 11)
        val noteId = nService.add(note)
        assertEquals(note, nService.getById(noteId))
    }

    @Test
    fun getCommentsTest() {
        val list: MutableList<NoteComments> = mutableListOf()
        val note = Notes(12, 12)
        val noteId = nService.add(note)
        for (i in 6..8L) {
            val comment = NoteComments(noteId, noteId, i, commentDate = note.date, message = "message$i")
            cService.add(comment)
            list.add(comment)
        }
        assertEquals(list, cService.read())
    }

    @Test
    fun getFriendsNotesTest() {
        val list: MutableList<Notes> = mutableListOf()
        for (i in 13..15L) {
            val note = Notes(i, i)
            nService.add(note)
            list.add(note)
        }
        assertEquals(3, list.size)
    }

    @Test
    fun restoreCommentTest() {
        val note = Notes(1, 1)
        val noteId = nService.add(note)
        val commentId = cService.add(NoteComments(noteId, noteId, 1, message = "message"))

        assertEquals(false, cService.getById(commentId).isDeleted)
        cService.delete(commentId)
        assertEquals(true, cService.getById(commentId).isDeleted)
    }
}
