package Posts

data class Photo(
    val id: Int = 0,
    val albumId: Int = 0,
    val ownerId: Int = 0,
    val userId: Int = 0,
)

data class Video(
    val id: Int = 0,
    val albumId: Int = 0,
    val ownerId: Int = 0,
    val userId: Int = 0,
)

data class Audio(
    val id: Int = 0,
    val ownerId: Int = 0,
    val artist: String = "artist",
    val title: String = "title",
    val duration: Int = 0,
    val albumId: Int = 0,
)

data class Doc(
    val id: Int = 0,
    val ownerId: Int = 0,
    val title: String = "doc",
    val size: Int = 0,
    val ext: String = ".docx",
)

data class Graffiti(
    val id: Int = 0,
    val ownerId: Int = 0,
    val photo130: String = "",
    val photo604: String = "",
)

sealed class Attachment(val type: String)

data class PhotoAttachment(val photo: Photo): Attachment("photo")
data class VideoAttachment(val video: Video): Attachment("video")
data class AudioAttachment(val audio: Audio): Attachment("audio")
data class DocAttachment(val doc: Doc): Attachment("doc")
data class GraffitiAttachment(val graffiti: Graffiti): Attachment("graffiti")