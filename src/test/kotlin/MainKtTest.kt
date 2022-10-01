import Posts.IdNotFoundException
import Posts.Post
import Posts.PostNotFoundException
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import kotlin.random.Random

class MainKtTest {

    //    Posts
    val wService = WallService
    @Before
    fun clear() {
        wService.clear()
    }

    @Test
    fun postIdChange() {
        val wsPost1 = wService.add(Post(111, 10, text = "content1", likes = Post.Likes(4)))

        assertEquals(1, wsPost1.id)
    }

    @Test
    fun negativeLikesToZero() {
        val wsPost1 = wService.add(Post(0, 10, text = "content1", likes = Post.Likes(-4)))

        assertEquals(0, wsPost1.likes.count)
    }

    @Test
    fun postUpdate() {
        val wsPost1 = wService.add(Post(0, 10, text = "content1", likes = Post.Likes(10)))
        val newPost = wsPost1.copy(text = "content2", likes = Post.Likes(14))

        assertEquals(true, wService.update(newPost))
    }

    @Test
    fun postNotUpdate() {
        val newPost = Post(49, 7, text = "content2", likes = Post.Likes(14))

        assertEquals(false, wService.update(newPost))
    }

    @Test
    fun printPosts() {
        for (i in 0 until 10) {
            val post = Post(fromId = Random.nextInt(100),
                text = "content",
                likes = Post.Likes(Random.nextInt(100)))
            wService.add(post)
        }
        wService.printPosts()
    }

    @Test
    fun isLiked() {
        val newPost = wService.add(Post(fromId = 17, text = "content", likes = Post.Likes(27)))
        val result = wService.likeById(newPost.id)

        assertEquals(28, result?.likes?.count)
    }

    @Test
    fun addComment() {
        val post = wService.add(Post())
        val comment = wService.createComment(post.id, Post.Comments(15))

        assertEquals(2, post.id)
        assertEquals(15, comment.id)
    }

    @Test(expected = PostNotFoundException::class)
    fun commentThrowException() {
        val commentException = wService.createComment(5, Post.Comments())
    }

    @Test(expected = IdNotFoundException::class)
    fun reportTest() {
        val post = wService.add(Post())
        val comment = wService.createComment(post.id, Post.Comments())
        wService.reportComment(post.ownerId, -1, 2)
    }
}