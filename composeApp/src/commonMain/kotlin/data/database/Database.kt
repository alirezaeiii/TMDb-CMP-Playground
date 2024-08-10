package data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PosterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun posterDao(): PosterDao
    override fun clearAllTables(): Unit {}
}

interface DB {
    fun clearAllTables(): Unit {}
}