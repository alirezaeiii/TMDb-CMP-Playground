package data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import domain.model.Poster

@Entity(tableName = "posters")
class PosterEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val release: String,
    val playtime: String,
    val description: String,
    val plot: String,
    val poster: String,
    val gif: String
)

fun List<PosterEntity>.asDomainModel(): List<Poster> {
    return map {
        Poster(
            id = it.id,
            name = it.name,
            release = it.release,
            playtime = it.playtime,
            description = it.description,
            plot = it.plot,
            poster = it.poster,
            gif = it.gif
        )
    }
}