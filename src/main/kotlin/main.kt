import java.time.LocalDateTime

data class Post(
    val id: Int = 0,
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
        count: Int = 0,
        var user_likes: Boolean = false,
        var can_like: Boolean = true,
        var can_publish: Boolean = true,
    ) {
        var count = count
            set(value) {
                if (value < 0) {
                    return
                }
                field = value
            }

        override fun toString(): String {
            return "(count=$count, user_likes=$user_likes, can_like=$can_like, can_publish=$can_publish)"
        }
    }
}

object WallService {
    private var posts = emptyArray<Post>()
    private var nextId = 1

    fun add(post: Post): Post {
        posts += if (post.likes.count < 0) post.copy(id = nextId, likes = Post.Likes(0)) else post.copy(id = nextId)
        nextId++
        return posts.last()
    }

    fun update(_post: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (_post.id == post.id) {
                posts[index] = post.copy(
//                    id = post.id,
                    from_id = post.from_id,
                    date = post.date,
                    text = _post.text,
                    likes = _post.likes,
                    views = _post.views,
                    can_pin = _post.can_pin,
                    can_delete = _post.can_delete,
                    can_edit = _post.can_edit,
                    marked_as_ads = _post.marked_as_ads,
                    is_favorite = _post.is_favorite)
                return true
            }
        }
        return false
    }

    fun likeById(id: Int) {
        for ((index, post) in posts.withIndex()) {
            if (post.id == id) {
                posts[index] = post.copy(likes = Post.Likes(post.likes.count + 1, true), views = post.views + 1)
            }
        }
    }

    fun printPosts() {
        for ((index, _) in posts.withIndex()) {
            println(posts[posts.size - 1 - index])
        }
    }
}

fun main() {
//      Массив постов
//    for (i in 0 until 10) {
//        val post = Post(from_id = Random.nextInt(100), date = LocalDateTime.now(), text = "content", likes = Post.Likes(Random.nextInt(100)))
//        WallService.add(post)
//    }

    val wsPost1 = WallService.add(Post(from_id = 48, date = LocalDateTime.now(), text = "content", likes = Post.Likes(-50)))
    val wsPost2 = WallService.add(Post(from_id = 19, date = LocalDateTime.now(), text = "content2", likes = Post.Likes(12)))

    WallService.likeById(wsPost1.id)
    WallService.likeById(wsPost1.id)
    WallService.likeById(wsPost1.id)
    WallService.likeById(wsPost2.id)
    WallService.likeById(wsPost2.id)
    WallService.printPosts()

//    Последние измененные параметры класса Likes применяются для всех постов
    WallService.update(wsPost1.copy(text = "testUpdate", likes = Post.Likes(user_likes = true), is_favorite = true))

    WallService.printPosts()
}