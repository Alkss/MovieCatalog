package com.alkss.moviecatalog.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alkss.moviecatalog.core.domain.model.local.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieListDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object {
        const val DATABASE_NAME = "movie_list_db"
    }
}
