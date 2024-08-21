package data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import domain.model.Movie

@Entity(tableName = "movies")
class MovieEntity(
    @PrimaryKey val id: Int,
    val overview: String,
    val releaseDate: String?,
    val backdropPath: String?,
    val name: String,
    val voteAverage: Double,
    val voteCount: Int
)

fun List<MovieEntity>.asDomainModel(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            overview = it.overview,
            releaseDate = it.releaseDate,
            backdropPath = it.backdropPath,
            name = it.name,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount
        )
    }
}