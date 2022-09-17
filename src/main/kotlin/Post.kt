import java.time.LocalDateTime

data class Post(
    val id: Int = 0,
    val ownerId: Int = 0,
    val fromId: Int = 0,
    val createdBy: Int = 0,
    val date: LocalDateTime = LocalDateTime.now(),
    val text: String = "content",
    val replyOwnerId: Int = 0,
    val replyPostId: Int = 0,
    val friendsOnly: Boolean = false,
    var comments: Comments? = null,
    var copyright: Copyright = Copyright(),
    val likes: Likes = Likes(),
    var reposts: Reposts? = null,
    val views: Views = Views(),
    val postType: PostType = PostType.post,
    val postSource: PostSource = PostSource(),
//    val attachments: Attachments,
    val geo: Geo? = null,
    val signerId: Int = 0,
//    val copyHistory: Array<Post>,
    val canPin: Boolean = false,
    val canDelete: Boolean = false,
    val canEdit: Boolean = false,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,
    val donut: Donut? = null,
    val postponedId: Int? = null,
) {
    data class Comments(
        val _count: Int = 0,
        val canPost: Boolean = true,
        val groupsCanPost: Boolean = true,
        val canClose: Boolean = false,
        val canOpen: Boolean = false,
    ) {
        var count = _count
            set(value) {
                if (value < 0) {
                    return
                }
                field = value
            }
    }

    data class Copyright(
        val id: Int = 0,
        val link: String = "",
        val name: String = "",
        val type: String = "",
    )

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

    data class Reposts(
        val _count: Int = 0,
        val userReposted: Boolean = false,
    ) {
        var count = _count
            set(value) {
                if (value < 0) {
                    return
                }
                field = value
            }
    }

    data class Views(
        val _count: Int = 0,
    ) {
        var count = _count
            set(value) {
                if (value < 0) {
                    return
                }
                field = value
            }
    }

    data class Geo(
        val type: String = "",
        val coordinates: String = "",
        val place: Place = Place(),
    ) {
        data class Place(
            val id: Int = 0,
            val title: String = "",
            val latitude: Int = 0,
            val longitude: Int = 0,
            val created: Int = 0,
            val icon: String = "",
            val checkins: Int = 0,
            val updated: Int = 0,
            val type: Int = 0,
            val country: Int = 0,
            val city: Int = 0,
            val address: String = "",
        )

    }

    enum class EditMode {
        all, duration
    }

    data class Donut(
        val isDonut: Boolean = false,
        val paidDuration: Int = 0,
        val placeholder: String = "Не оформлена подписка VK Donut.",
        val canPublishFreeCopy: Boolean = false,
        val editMode: EditMode = EditMode.all,
    )
}