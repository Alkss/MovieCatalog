package com.alkss.moviecatalog.core.domain.model.remote

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("original_title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("vote_average")
    val rating: Double,
)

data class Results(
    @SerializedName("results")
    val movieList: List<MovieResponse>
)
