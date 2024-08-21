package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val overview: String,
    val releaseDate: String?,
    val backdropPath: String?,
    val name: String,
    val voteAverage: Double,
    val voteCount: Int
)