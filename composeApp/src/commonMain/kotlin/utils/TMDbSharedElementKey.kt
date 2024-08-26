package utils

data class TMDbSharedElementKey(
    val tmdbId: Int,
    val type: TMDbSharedElementType
)

enum class TMDbSharedElementType {
    Bounds,
    Image,
    Title,
    ReleaseDate,
    Rate,
    VOTE
}