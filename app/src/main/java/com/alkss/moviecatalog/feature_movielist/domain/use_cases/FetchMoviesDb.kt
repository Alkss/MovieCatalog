package com.alkss.moviecatalog.feature_movielist.domain.use_cases

import com.alkss.moviecatalog.core.data.repository.MovieListLocalRepository
import com.alkss.moviecatalog.core.domain.model.local.Movie
import javax.inject.Inject

class FetchMoviesDb @Inject constructor(
    private val localRepository: MovieListLocalRepository
) {
    operator fun invoke(limit: Int, offset: Int): List<Movie> {
        return localRepository.getAllMovies(limit = limit, offset = offset)
    }
}
