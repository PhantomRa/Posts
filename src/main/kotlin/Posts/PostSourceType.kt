package Posts

enum class PostSourceType {
    vk, widget, api, rss, sms
}

enum class PostSourcePlatform {
    android, iphone, wphone
}

data class PostSource(
    val type: PostSourceType = PostSourceType.vk,
    val platform: PostSourcePlatform = PostSourcePlatform.android,
    val data: String = "",
    val url: String = "",
)