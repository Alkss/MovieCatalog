package com.alkss.moviecatalog.feature_movielist.domain.use_cases

data class HomeUseCases(
    val fetchMovieListRequest: FetchMovieListRequest,
    val fetchMoviesDb: FetchMoviesDb,
    val updateFavoriteState: UpdateFavoriteState
)
