package com.alkss.moviecatalog.feature_movielist.domain.use_cases

import com.alkss.moviecatalog.core.data.remote.MyAPI
import com.alkss.moviecatalog.core.data.repository.MovieListLocalRepository
import com.alkss.moviecatalog.core.data.repository.MovieListRemoteRepository
import com.alkss.moviecatalog.core.data.util.NetworkResult
import com.alkss.moviecatalog.core.domain.model.local.Movie
import com.alkss.moviecatalog.core.domain.model.remote.MovieResponse
import javax.inject.Inject

class FetchMovieListRequest @Inject constructor(
    private val localRepository: MovieListLocalRepository,
    private val remoteRepository: MovieListRemoteRepository
){
    suspend operator fun invoke(currentPage: Int): List<Movie>{
        return when (val response = remoteRepository.getMovieList(currentPage = currentPage)){
            is NetworkResult.Error -> emptyList()
            is NetworkResult.Success -> {
                //todo this might be an issue, come back here to look at it
                val list = response.data.map { it.movieList }[0]
                insertListIntoDB(list)
            }
        }
    }

    private fun insertListIntoDB(movieList: List<MovieResponse>): List<Movie> {
        val localMovieList: MutableList<Movie> = mutableListOf()
        movieList.forEach {
            localMovieList.add(
                mapFromMovieResponse(it)
            )
        }
        localRepository.insertMovieList(localMovieList)
        return localMovieList.toList()
    }

    private fun mapFromMovieResponse(movieResponse: MovieResponse): Movie {
        return Movie(
            id = movieResponse.id,
            title = movieResponse.title,
            overview = movieResponse.overview,
            posterPath = MyAPI.POSTER_ROOT_PATH + movieResponse.posterPath,
            releaseDate = movieResponse.releaseDate,
            rating = movieResponse.rating,
            isFavorite = false
        )
    }
}
