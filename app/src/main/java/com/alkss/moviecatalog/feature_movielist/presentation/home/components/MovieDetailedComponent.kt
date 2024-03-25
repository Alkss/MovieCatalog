package com.alkss.moviecatalog.feature_movielist.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alkss.moviecatalog.feature_movielist.presentation.home.MovieUiState

@Composable
fun MovieDetailedComponent(
    modifier: Modifier = Modifier,
    movie: MovieUiState,
    onFavoriteClick: (movieId: Int) -> Unit
) {
    val isFavorite = remember {
        mutableStateOf(movie.isFavorite)
    }

    val scrollState = rememberScrollState()

    Card(modifier = modifier.padding(16.dp)) {
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)) {
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
            Text(text = movie.movieOverview)

            if (isFavorite.value){
                TextButton(onClick = {
                    isFavorite.value = false
                    onFavoriteClick.invoke(movie.movieId)
                }) {
                    Text(text = "Remove from favorite")
                }
            }else{
                TextButton(onClick = {
                    isFavorite.value = true
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
private fun MovieDetailPreview() {
    val movie = MovieUiState(
        movieId = 4573,
        movieTitle = "sumo",
        movieOverview = "platea",
        posterPath = "non",
        releaseDate = "efficitur",
        rating = 18.19,
        isFavorite = false
    )
    MovieDetailedComponent(movie = movie) {

    }
}