object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Post.Comments>()
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

    fun printPosts() {
        for ((index, _) in posts.withIndex()) {
            println(posts[posts.size - 1 - index])
        }
    }
}

fun main() {
    val service = WallService
    val post = service.add(Post())
    val comment = service.createComment(post.id, Post.Comments(15, groupsCanPost = false))
    println(comment)

//    val commentException = service.createComment(5, Post.Comments())
//    println(commentException)
}