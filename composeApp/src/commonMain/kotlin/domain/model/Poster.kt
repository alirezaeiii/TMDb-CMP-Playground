package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Poster(
    val id: Long,
    val name: String,
    val release: String,
    val playtime: String,
    val description: String,
    val plot: String,
    val poster: String,
    val gif: String
)