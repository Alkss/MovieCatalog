package com.alkss.moviecatalog.core.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alkss.moviecatalog.core.domain.model.local.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY id LIMIT :limit OFFSET :offset")
    fun getAllMovies(limit: Int, offset: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE isFavorite=1 LIMIT :limit OFFSET :offset")
    fun getAllFavorites(limit: Int, offset: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovieById(id: Int): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMovieList(movieList: List<Movie>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateMovieFavorite(movie: Movie)
}
