import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import kotlin.random.Random

class MainKtTest {

    @Before
    fun clear() {
        WallService.clear()
    }

    @Test
    fun postIdChange() {
        val wsPost1 = WallService.add(Post(111, 10, text = "content1", likes = Post.Likes(4)))

        assertEquals(1, wsPost1.id)
    }

    @Test
    fun negativeLikesToZero() {
        val wsPost1 = WallService.add(Post(0, 10, text = "content1", likes = Post.Likes(-4)))

        assertEquals(0, wsPost1.likes.count)
    }

    @Test
    fun postUpdate() {
        val wsPost1 = WallService.add(Post(0, 10, text = "content1", likes = Post.Likes(10)))
        val newPost = wsPost1.copy(text = "content2", likes = Post.Likes(14))

        assertEquals(true, WallService.update(newPost))
    }

    @Test
    fun postNotUpdate() {
        val newPost = Post(49, 7, text = "content2", likes = Post.Likes(14))

        assertEquals(false, WallService.update(newPost))
    }

    @Test
    fun printPosts() {
        for (i in 0 until 10) {
            val post = Post(fromId = Random.nextInt(100),
                text = "content",
                likes = Post.Likes(Random.nextInt(100)))
            WallService.add(post)
        }
        WallService.printPosts()
    }

    @Test
    fun isLiked() {
        val newPost = WallService.add(Post(fromId = 17, text = "content", likes = Post.Likes(27)))
        val result = WallService.likeById(newPost.id)

        assertEquals(28, result?.likes?.count)
    }

    @Test
    fun addComment() {
        val service = WallService
        val post = service.add(Post())
        val comment = service.createComment(post.id, Post.Comments(15))

        assertEquals(2, post.id)
        assertEquals(Post.Comments(15), comment)
    }

    @Test(expected = PostNotFoundException::class)
    fun commentThrowException() {
        val service = WallService
        val commentException = service.createComment(5, Post.Comments())
    }
}