package com.alkss.moviecatalog.core.data.repository

import com.alkss.moviecatalog.core.data.data_source.MovieDao
import com.alkss.moviecatalog.core.domain.model.local.Movie
import javax.inject.Inject

class MovieListLocalRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieListLocalRepository {
    override fun getAllMovies(limit: Int, offset: Int): List<Movie> {
        return movieDao.getAllMovies(limit, offset)
    }

    override fun getMovieById(id: Int): Movie {
        return movieDao.getMovieById(id)
    }

    override fun getAllFavorites(limit: Int, offset: Int): List<Movie> {
        return movieDao.getAllFavorites(limit, offset)
    }

    override fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    override fun insertMovieList(movieList: List<Movie>) {
        movieDao.insertMovieList(movieList)
    }

    override fun updateMovieFavorite(movieId: Int, isFavorite: Boolean) {
        val movie = getMovieById(movieId)
        movieDao.updateMovieFavorite(movie.copy(isFavorite = isFavorite))
    }
}