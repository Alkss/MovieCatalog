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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    private val _offset = MutableStateFlow(0)

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.ForceRefresh -> refreshMoviesAndUpdateState()
            HomeEvent.NextPage -> fetchMoviesAndUpdateState()
            is HomeEvent.ChangeFavoriteState -> {
                updateFavoriteAndState(event.movieId)
            }
        }
    }

    private fun updateFavoriteAndState(movieId: Int) {
        job = viewModelScope.launch(Dispatchers.IO) {
            if (job?.isCancelled == true) {
                return@launch
            }
            //get the movie
            val movie = _uiState.value.movieList.first { originalUiState ->
                originalUiState.movieId == movieId
            }
            val updatedMovie = movie.copy(isFavorite = !movie.isFavorite)
            //update movie in the database
            homeUseCases.updateFavoriteState.invoke(updatedMovie.movieId, updatedMovie.isFavorite)

            //update movie from favorite list
            _uiState.update { currentUiState ->
                val updatedMovieList = currentUiState.movieList.map {
                    if (it.movieId == movie.movieId)
                        it.copy(isFavorite = movie.isFavorite)
                    else
                        it
                }
                val updatedFavoriteList =
                    if (movie.isFavorite)
                        currentUiState.favoriteList + updatedMovie
                    else
                        currentUiState.favoriteList.filter { it.movieId != movie.movieId }

                currentUiState.copy(
                    movieList = updatedMovieList.distinct(),
                    favoriteList = updatedFavoriteList.distinct()
                )

            }
        }
    }

    private fun fetchMoviesAndUpdateState() {
        job = viewModelScope.launch(Dispatchers.IO) {
            if (job?.isCancelled == true) {
                return@launch
            }

            val offset = _offset.value
            val page = offset.offsetToPage()
            var movieList = homeUseCases.fetchMoviesDb.invoke(limit = 20, offset = offset)

            // If the size of the object returned by the db is less than 20, fetch the rest from the remote API
            if (movieList.size < 20) {
                movieList += homeUseCases.fetchMovieListRequest.invoke(page)
            }

            // If the data from the database is empty when going to the next offset, fetch data from the API
            if (offset % 20 == 0 && movieList.isEmpty()) {
                movieList = homeUseCases.fetchMovieListRequest.invoke(page)
            }

            updateUiState(movieList)

            // Increment the offset for the next fetch operation
            _offset.update { it + 19 }
        }
    }


    private fun refreshMoviesAndUpdateState() {
        job = viewModelScope.launch(Dispatchers.IO) {
            if (job?.isCancelled == true) {
                return@launch
            }

            val page = _offset.value.offsetToPage()
            val movieList = homeUseCases.fetchMovieListRequest.invoke(page)
            updateUiState(movieList)
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
    }
}
