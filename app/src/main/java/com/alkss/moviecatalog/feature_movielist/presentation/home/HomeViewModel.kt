package com.alkss.moviecatalog.feature_movielist.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alkss.moviecatalog.core.domain.model.local.Movie
import com.alkss.moviecatalog.feature_movielist.domain.use_cases.HomeUseCases
import com.alkss.moviecatalog.feature_movielist.presentation.util.offsetToPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(AtomicBoolean(false))
    val isLoading = _isLoading.asStateFlow()

    private var job: Job? = null
    private var _currentPointer = 0

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ForceRefresh -> refreshMoviesAndUpdateState()
            HomeEvent.NextPage -> {
                _currentPointer += 20
                refreshMoviesAndUpdateState()
            }
            is HomeEvent.ChangeFavoriteState -> updateFavoriteAndState(event.movieId)
            is HomeEvent.SortMovieList -> sortList(event.sortType, event.sortOrder)
        }
    }

    private fun sortList(
        sortType: SortType = SortType.RATING, sortOrder: SortOrder = SortOrder.ASCENDING
    ) {
        _uiState.update { uiState ->
            val sortedMovies = when (sortType) {
                SortType.RATING -> if (sortOrder == SortOrder.ASCENDING)
                    uiState.movieList.sortedBy { it.rating }
                else
                    uiState.movieList.sortedByDescending { it.rating }

                SortType.NAME -> if (sortOrder == SortOrder.ASCENDING)
                    uiState.movieList.sortedBy { it.movieTitle }
                else
                    uiState.movieList.sortedByDescending { it.movieTitle }

                SortType.RELEASE_DATE -> if (sortOrder == SortOrder.ASCENDING)
                    uiState.movieList.sortedBy { it.releaseDate }
                else
                    uiState.movieList.sortedByDescending { it.releaseDate }
            }
            val sortedFavorites = when (sortType) {
                SortType.RATING -> if (sortOrder == SortOrder.ASCENDING)
                    uiState.favoriteList.sortedBy { it.rating }
                else
                    uiState.favoriteList.sortedByDescending { it.rating }

                SortType.NAME -> if (sortOrder == SortOrder.ASCENDING)
                    uiState.favoriteList.sortedBy { it.movieTitle }
                else
                    uiState.favoriteList.sortedByDescending { it.movieTitle }

                SortType.RELEASE_DATE -> if (sortOrder == SortOrder.ASCENDING)
                    uiState.favoriteList.sortedBy { it.releaseDate }
                else
                    uiState.favoriteList.sortedByDescending { it.releaseDate }
            }

            uiState.copy(
                movieList = sortedMovies, favoriteList = sortedFavorites
            )
        }
    }

    private fun updateFavoriteAndState(movieId: Int) {
        job = viewModelScope.launch(Dispatchers.IO) {
            if (job?.isCancelled == true) {
                return@launch
            }
            // get the movie
            val movie = _uiState.value.movieList.first { originalUiState ->
                originalUiState.movieId == movieId
            }
            val updatedMovie = movie.copy(isFavorite = !movie.isFavorite)
            // update movie in the database
            homeUseCases.updateFavoriteState.invoke(updatedMovie.movieId, updatedMovie.isFavorite)

            // update movie from favorite list
            _uiState.update { currentUiState ->
                val updatedMovieList = currentUiState.movieList.map {
                    if (it.movieId == updatedMovie.movieId) it.copy(isFavorite = updatedMovie.isFavorite)
                    else it
                }
                val updatedFavoriteList =
                    if (updatedMovie.isFavorite) currentUiState.favoriteList + updatedMovie
                    else currentUiState.favoriteList.filter { it.movieId != updatedMovie.movieId }

                currentUiState.copy(
                    movieList = updatedMovieList.distinct(),
                    favoriteList = updatedFavoriteList.distinct()
                )

            }
        }
    }


    private fun refreshMoviesAndUpdateState() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _isLoading.update { AtomicBoolean(true) }
            if (job?.isCancelled == true) {
                return@launch
            }

            val page = _currentPointer.offsetToPage()
            val movieList = homeUseCases.fetchMovieListRequest.invoke(page)
            updateUiState(movieList)
            _isLoading.update { AtomicBoolean(false) }
        }
    }

    private fun updateUiState(movieList: List<Movie>) {
        val newMovies = HomeUiState().mapFrom(movieList)
        _uiState.update {
            it.copy(
                movieList = (it.movieList + newMovies.movieList).distinct(),
                favoriteList = (it.favoriteList + newMovies.favoriteList).distinct()
            )
        }
        sortList()
    }
}
