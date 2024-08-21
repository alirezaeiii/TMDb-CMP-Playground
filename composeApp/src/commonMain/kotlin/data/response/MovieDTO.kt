package data.response

import data.database.MovieEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TMDbWrapper(
    @SerialName("results")
    val items: List<MovieDTO>
)

@Serializable
data class MovieDTO(
    val id: Int,
    val overview: String,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("title")
    val name: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
)

fun List<MovieDTO>.asDatabaseModel(): Array<MovieEntity> {
    return map {
        MovieEntity(
            id = it.id,
            overview = it.overview,
            releaseDate = it.releaseDate,
            it.backdropPath?.let { backdropPath ->
                "http://image.tmdb.org/t/p/w780$backdropPath"
            },
            name = it.name,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount
        )
    }.toTypedArray()
}

