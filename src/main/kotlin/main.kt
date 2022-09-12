import java.time.LocalDateTime

data class Post(
    val id: Int,
    val from_id: Int,
    val date: LocalDateTime,
    val text: String,
    var likes: Likes,
    val views: Int = 0,
    val can_pin: Boolean = false,
    val can_delete: Boolean = false,
    val can_edit: Boolean = false,
    val marked_as_ads: Boolean = false,
    val is_favorite: Boolean = false,
) {
    class Likes(
        _count: Int = 0,
        var user_likes: Boolean = false,
        var can_like: Boolean = true,
        var can_publish: Boolean = true,
    ) {
        var count: Int = _count
            set(value) {
                if (value > 0) field = value
            }

        fun incLikes() {
            count++
        }

        override fun toString(): String {
            return "(count=$count, user_likes=$user_likes, can_like=$can_like, can_publish=$can_publish)"
        }
    }

}

object WallService {
    private var posts = emptyArray<Post>()

    fun add(post: Post): Post {
        posts += post
        return posts.last()
    }

    fun likeById(id: Int) {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                post.likes.incLikes()
                posts[index] = post.copy(likes = post.likes, views = post.views + 1)
            }
        }
    }

    fun viewed(id: Int) {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                posts[index] = post.copy(views = post.views + 1)
            }
        }
    }

    fun toString(post: Post): String {
        return posts[post.id].toString()
    }
}

fun main() {

//        TODO Сделать получение текущего времени
    val dateNow = LocalDateTime.now()
    val post1 = Post(0, 0, dateNow, "content${0}", Post.Likes())
    val post2 = Post(1, 1, dateNow, "content${1}", Post.Likes())
    WallService.add(post1)
    WallService.add(post2)

    WallService.viewed(post1.id)
    WallService.viewed(post2.id)

    WallService.likeById(post1.id)
    WallService.likeById(post1.id)
    WallService.likeById(post1.id)
    WallService.likeById(post2.id)
    WallService.likeById(post2.id)

    WallService.viewed(post2.id)
    WallService.viewed(post2.id)

    println(WallService.toString(post1))
    println(WallService.toString(post2))
}