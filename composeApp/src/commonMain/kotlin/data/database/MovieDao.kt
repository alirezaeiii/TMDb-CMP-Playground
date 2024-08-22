package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    /**
     * Select all from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<MovieEntity>

    /**
     * Insert movies in the database. If the movie already exists, replace it.
     *
     * @param movies the movies to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg movies: MovieEntity)
}