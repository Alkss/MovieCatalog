package com.alkss.moviecatalog.core.data.remote.endpoint

import com.alkss.moviecatalog.core.domain.model.remote.Results
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBApi {

    @GET("/discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int = 1
    ): List<Results>
}
