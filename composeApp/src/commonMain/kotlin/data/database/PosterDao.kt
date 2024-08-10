package data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PosterDao {

    /**
     * Select all from the posters table.
     *
     * @return all posters.
     */
    @Query("SELECT * FROM posters")
    suspend fun getPosters(): List<PosterEntity>

    /**
     * Insert posters in the database. If the poster already exists, replace it.
     *
     * @param posters the posters to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg posters: PosterEntity)
}