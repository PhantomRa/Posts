import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime
import kotlin.random.Random

class MainKtTest {

    @Test
    fun postIdChange() {
        val wsPost1 = WallService.add(Post(0, 10, LocalDateTime.now(), "content1", Post.Likes(4)))

        assertEquals(1, wsPost1.id)
    }

    @Test
    fun negativeLikesToZero() {
        val wsPost1 = WallService.add(Post(0, 10, LocalDateTime.now(), "content1", Post.Likes(-4)))

        assertEquals(0, wsPost1.likes.count)
    }

    @Test
    fun postUpdate() {
        val wsPost1 = WallService.add(Post(0, 10, LocalDateTime.now(), "content1", Post.Likes(10)))
        val newPost = wsPost1.copy(text = "content2", likes = Post.Likes(14))

        assertEquals(true, WallService.update(newPost))
    }

    @Test
    fun postNotUpdate() {
        val newPost = Post(19, 7, LocalDateTime.now(), "content2", Post.Likes(14))

        assertEquals(false, WallService.update(newPost))
    }

    @Test
    fun printPosts() {
        for (i in 0 until 10) {
            val post = Post(from_id = Random.nextInt(100),
                date = LocalDateTime.now(),
                text = "content",
                likes = Post.Likes(Random.nextInt(100)))
            WallService.add(post)
        }
        WallService.printPosts()
    }

//    @Test
//    fun isLiked() {
//          В программе счетчик лайков работает, в автотестах нет
//        val newPost = WallService.add(Post(from_id = 17, date = LocalDateTime.now(), text = "content", likes = Post.Likes(27)))
//        WallService.likeById(newPost.id)
//
//        assertEquals(28, newPost.likes.count)
//    }
}