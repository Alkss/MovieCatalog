package com.alkss.moviecatalog.core.data.repository

import com.alkss.moviecatalog.core.data.util.NetworkResult
import com.alkss.moviecatalog.core.domain.model.remote.Results

fun interface MovieListRemoteRepository {

    suspend fun getMovieList(currentPage: Int): NetworkResult<List<Results>>
}