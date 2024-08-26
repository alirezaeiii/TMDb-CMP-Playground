package utils

data class TMDbSharedElementKey(
    val movieId: Int,
    val type: TMDbSharedElementType
)

enum class TMDbSharedElementType {
    Bounds,
    Image,
    Title,
    ReleaseDate,
    VOTE
}