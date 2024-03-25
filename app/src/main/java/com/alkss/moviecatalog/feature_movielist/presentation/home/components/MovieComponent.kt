package com.alkss.moviecatalog.feature_movielist.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alkss.moviecatalog.feature_movielist.presentation.home.MovieUiState

@Composable
fun MovieComponent(
    modifier: Modifier = Modifier,
    movie: MovieUiState,
    onFavoriteClick: (movieId: Int) -> Unit
) {
    Card(modifier = modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(4.dp)) {
            AsyncImage(
                model = movie.posterPath,
                contentDescription = movie.movieTitle,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.movieTitle,
                modifier.align(Alignment.CenterHorizontally),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Release: ${movie.releaseDate}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Rating: ${movie.rating}/10", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (movie.isFavorite){
                TextButton(onClick = {
                    onFavoriteClick.invoke(movie.movieId)
                }) {
                    Text(text = "Remove from favorite")
                }
            }else{
                TextButton(onClick = {
                    onFavoriteClick.invoke(movie.movieId)
                }) {
                    Text(text = "Add to favorite")
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieComponentPreview() {
    val uiState = MovieUiState(
        movieId = 5093,
        movieTitle = "The return of Jedi",
        movieOverview = "delectus",
        posterPath = "https://image.tmdb.org/t/p/w500/qhb1qOilapbapxWQn9jtRCMwXJF.jpg",
        releaseDate = "2020/02/02",
        rating = 6.721,
        isFavorite = false
    )
    MovieComponent(movie = uiState){}
}