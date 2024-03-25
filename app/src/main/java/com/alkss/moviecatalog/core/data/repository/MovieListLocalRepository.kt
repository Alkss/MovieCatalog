package com.alkss.moviecatalog.core.data.repository

import com.alkss.moviecatalog.core.domain.model.local.Movie

interface MovieListLocalRepository {

    fun getAllMovies(limit: Int, offset: Int): List<Movie>

    fun getMovieById(id: Int): Movie

    fun getAllFavorites(limit: Int, offset: Int): List<Movie>

    fun insertMovie(movie: Movie)

    fun updateMovieFavorite(movieId: Int, isFavorite: Boolean)

    fun insertMovieList(movieList: List<Movie>)
}