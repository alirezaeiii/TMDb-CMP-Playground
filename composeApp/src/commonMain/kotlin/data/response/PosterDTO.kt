package data.response

import domain.model.Poster
import kotlinx.serialization.Serializable

@Serializable
data class PosterDto(
    val id: Long,
    val name: String,
    val release: String,
    val playtime: String,
    val description: String,
    val plot: String,
    val poster: String,
    val gif: String
)

fun List<PosterDto>.asDomainModel() = map {
    Poster(
        id = it.id, name = it.name, release = it.release, playtime = it.playtime,
        description = it.description, plot = it.plot, poster = it.poster, gif = it.gif
    )
}