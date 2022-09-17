import java.time.LocalDateTime

data class Post(
    val id: Int = 0,
    val fromId: Int,
    val date: LocalDateTime = LocalDateTime.now(),
    val text: String,
    val likes: Likes = Likes(),
    val views: Int = 0,
    val canPin: Boolean = false,
    val canDelete: Boolean = false,
    val canEdit: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,
) {
    data class Likes(
        val _count: Int = 0,
        val userLikes: Boolean = false,
        val canLike: Boolean = true,
        val canPublish: Boolean = true,
    ) {
        var count = _count
            set(value) {
                if (value < 0) {
                    return
                }
                field = value
            }
    }
}