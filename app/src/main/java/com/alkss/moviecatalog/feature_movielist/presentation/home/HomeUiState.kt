package com.alkss.moviecatalog.feature_movielist.presentation.home

import com.alkss.moviecatalog.core.domain.model.local.Movie

data class HomeUiState(
    val movieList: List<MovieUiState> = emptyList(),
    val favoriteList: List<MovieUiState> = emptyList()
){
    companion object fun mapFrom(movies: List<Movie>): HomeUiState{
        val movieList = movies.map { movie ->
            MovieUiState(
                movieId = movie.id,
                movieTitle = movie.title,
                movieOverview = movie.overview,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                rating = movie.rating,
                isFavorite = movie.isFavorite
            )
        }
        val favoriteList = movieList.filter { movie -> movie.isFavorite }

        return HomeUiState(
            movieList = movieList,
            favoriteList = favoriteList
        )
    }
}

data class MovieUiState(
    val movieId: Int,
    val movieTitle: String,
    val movieOverview: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Float,
    val isFavorite: Boolean
)