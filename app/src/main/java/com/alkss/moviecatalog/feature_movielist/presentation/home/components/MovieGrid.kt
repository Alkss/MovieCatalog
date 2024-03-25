package com.alkss.moviecatalog.feature_movielist.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alkss.moviecatalog.feature_movielist.presentation.home.MovieUiState

@Composable
fun MovieGrid(
    modifier: Modifier = Modifier,
    uiState: List<MovieUiState>,
    onMovieClick: (movie: MovieUiState) -> Unit,
    onFavoriteChange: (movieId: Int) -> Unit,
    onNextPage: () -> Unit
) {
    var offset by remember {
        mutableIntStateOf(8)
    }
    val lazyListState = rememberLazyListState()

    LazyColumn(state = lazyListState) {
        itemsIndexed(uiState.chunked(2)) { index, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                rowItems.forEach {
                    MovieComponent(
                        movie = it,
                        onFavoriteClick = { movieId -> onFavoriteChange.invoke(movieId) },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onMovieClick(it) }
                    )
                }
                if (uiState.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (index >= offset) {
                LaunchedEffect(Unit) {
                    onNextPage()
                    offset += 10
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieGridPreview() {
    val uiState = listOf(
        MovieUiState(
            movieId = 6189,
            movieTitle = "hac",
            movieOverview = "invidunt",
            posterPath = "amet",
            releaseDate = "2020/20/20",
            rating = 2.3,
            isFavorite = false
        ),
        MovieUiState(
            movieId = 6189,
            movieTitle = "hac",
            movieOverview = "invidunt",
            posterPath = "amet",
            releaseDate = "2020/20/20",
            rating = 2.3,
            isFavorite = false
        ),
        MovieUiState(
            movieId = 6189,
            movieTitle = "hac",
            movieOverview = "invidunt",
            posterPath = "amet",
            releaseDate = "2020/20/20",
            rating = 2.3,
            isFavorite = false
        ),
    )
    MovieGrid(uiState = uiState, onMovieClick = {}, onFavoriteChange = {}, onNextPage = {})

}
