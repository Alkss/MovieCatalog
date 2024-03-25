package com.alkss.moviecatalog.core.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Float,
    val isFavorite: Boolean = false
)
