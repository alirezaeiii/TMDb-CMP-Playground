package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    /**
     * Select all from the posters table.
     *
     * @return all posters.
     */
    @Query("SELECT * FROM movies")
    suspend fun getMovies(): List<MovieEntity>

    /**
     * Insert posters in the database. If the poster already exists, replace it.
     *
     * @param movies the movies to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg movies: MovieEntity)
}