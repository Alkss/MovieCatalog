package com.alkss.moviecatalog.core.data.repository

import android.util.Log
import com.alkss.moviecatalog.core.data.remote.endpoint.MovieDBApi
import com.alkss.moviecatalog.core.data.util.NetworkResult
import com.alkss.moviecatalog.core.domain.model.remote.Results
import retrofit2.Retrofit
import javax.inject.Inject

class MovieListRemoteRepositoryImpl @Inject constructor(
    retrofit: Retrofit
) : MovieListRemoteRepository {
    private val api = retrofit.create(MovieDBApi::class.java)
    override suspend fun getMovieList(currentPage: Int): NetworkResult<Results> {
        try {
            val response = api.getMovies(currentPage)
            return NetworkResult.Success(response)
        } catch (e: Exception) {
            Log.d("MovieListAPI", "Failed to retrieve movies from API: ${e.message}")
            return NetworkResult.Error(e)
        }
    }
}
