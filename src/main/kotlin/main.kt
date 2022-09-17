object WallService {
    private var posts = emptyArray<Post>()
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
                    post.likes.canPublish), views = post.views + 1)
                return posts[index]
            }
        }
        return null
    }

    fun printPosts() {
        for ((index, _) in posts.withIndex()) {
            println(posts[posts.size - 1 - index])
        }
    }
}

fun main() {
    val wsPost1 =
        WallService.add(Post(fromId = 48, text = "content", likes = Post.Likes(-50, canPublish = false)))
    val wsPost2 =
        WallService.add(Post(fromId = 19, text = "content2", likes = Post.Likes(12)))

    WallService.update(wsPost1.copy(text = "testUpdate", isFavorite = true))
    WallService.update(wsPost2.copy(text = "testUpdate2", canPin = true))

    println(WallService.likeById(wsPost1.id))
    println(WallService.likeById(wsPost1.id))
    println(WallService.likeById(wsPost1.id))
    println(WallService.likeById(wsPost2.id))
    println(WallService.likeById(wsPost2.id))

    println(WallService.likeById(10))
}