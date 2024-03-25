package com.alkss.moviecatalog.feature_movielist.domain.use_cases

import com.alkss.moviecatalog.core.data.repository.MovieListLocalRepository
import javax.inject.Inject

class UpdateFavoriteState @Inject constructor(
    private val localRepository: MovieListLocalRepository
) {
    operator fun invoke(movieId: Int, isFavorite: Boolean) {
        localRepository.updateMovieFavorite(movieId, isFavorite)
    }
}
