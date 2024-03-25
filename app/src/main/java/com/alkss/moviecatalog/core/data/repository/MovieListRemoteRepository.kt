package com.alkss.moviecatalog.core.data.repository

import com.alkss.moviecatalog.core.NetworkResult
import com.alkss.moviecatalog.core.domain.model.remote.MovieResponse

fun interface MovieListRemoteRepository {

    suspend fun getMovieList(currentPage: Int): NetworkResult<List<MovieResponse>>
}