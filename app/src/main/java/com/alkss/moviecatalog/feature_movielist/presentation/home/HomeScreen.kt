package com.alkss.moviecatalog.feature_movielist.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.alkss.moviecatalog.feature_movielist.presentation.home.components.LoadingSpinner
import com.alkss.moviecatalog.feature_movielist.presentation.home.components.MovieDetailedComponent
import com.alkss.moviecatalog.feature_movielist.presentation.home.components.MovieGrid

@Composable
fun HomeScreen(
    uiState: HomeUiState, isLoading: Boolean, action: (HomeEvent) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var showDialog = remember { mutableStateOf(false) }
    val sortOrderName = remember {
        mutableStateOf(SortOrder.DESCENDING)
    }
    val sortOrderRating = remember {
        mutableStateOf(SortOrder.DESCENDING)
    }
    val sortOrderRelease = remember {
        mutableStateOf(SortOrder.DESCENDING)
    }
    val selectedMovie = remember {
        mutableStateOf<MovieUiState?>(null)
    }

    val tabs = listOf("Movies", "Favorites")



    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index })
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            SortButton(filterButton = FilterButton.NAME, sortOrder = sortOrderName, action = {
                action.invoke(it)
            })

            SortButton(filterButton = FilterButton.RATING, sortOrder = sortOrderRating, action = {
                action.invoke(it)
            })
            SortButton(filterButton = FilterButton.RELEASE_DATE,
                sortOrder = sortOrderRelease,
                action = {
                    action.invoke(it)
                })
        }
        Box(Modifier.fillMaxSize()) {
            if (isLoading) {
                LoadingSpinner()
            }

            when (tabIndex) {
                0 -> {
                    if (uiState.movieList.isEmpty() && !isLoading) {
                        Card(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                modifier = Modifier.padding(24.dp),
                                text = "You dont have any movies here"
                            )
                        }
                    } else {
                        MovieGrid(
                            uiState = uiState.movieList,
                            onMovieClick = { movie ->
                                selectedMovie.value = movie
                                showDialog.value = true
                            }, onFavoriteChange = {
                                action.invoke(
                                    HomeEvent.ChangeFavoriteState(movieId = it)
                                )
                            }, onNextPage = {
                                action.invoke(HomeEvent.NextPage)
                            }
                        )
                    }
                }

                1 -> {
                    if (uiState.favoriteList.isEmpty() && !isLoading) {
                        Card(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                modifier = Modifier.padding(24.dp),
                                text = "You dont have any movies here"
                            )
                        }
                    } else {
                        MovieGrid(
                            uiState = uiState.favoriteList,
                            onMovieClick = { movie ->
                                selectedMovie.value = movie
                                showDialog.value = true
                            }, onFavoriteChange = {
                                action.invoke(
                                    HomeEvent.ChangeFavoriteState(movieId = it)
                                )
                            }, onNextPage = {
                                action.invoke(HomeEvent.NextPage)
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    action.invoke(HomeEvent.ForceRefresh)
                }, modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                Text(text = "Refresh", color = MaterialTheme.colorScheme.inversePrimary)
                Image(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                )
            }
        }

        MovieDetailDialog(showDialog, selectedMovie, action)
    }
}

@Composable
fun SortButton(
    modifier: Modifier = Modifier,
    filterButton: FilterButton,
    action: (HomeEvent) -> Unit,
    sortOrder: MutableState<SortOrder>
) {
    when (filterButton) {
        FilterButton.NAME -> {
            TextButton(onClick = {
                action.invoke(
                    HomeEvent.SortMovieList(sortType = SortType.NAME, sortOrder = sortOrder.value)
                )
                sortOrder.value =
                    if (SortOrder.ASCENDING == sortOrder.value) SortOrder.DESCENDING else SortOrder.ASCENDING
            }) {
                Text(text = "Sort by name")
            }
        }

        FilterButton.RATING -> {
            TextButton(onClick = {
                action.invoke(
                    HomeEvent.SortMovieList(
                        sortType = SortType.RATING, sortOrder = sortOrder.value
                    )
                )
                sortOrder.value =
                    if (SortOrder.ASCENDING == sortOrder.value) SortOrder.DESCENDING else SortOrder.ASCENDING
            }) {
                Text(text = "Sort by rating")
            }
        }

        FilterButton.RELEASE_DATE -> {
            TextButton(onClick = {
                action.invoke(
                    HomeEvent.SortMovieList(
                        sortType = SortType.RELEASE_DATE, sortOrder = sortOrder.value
                    )
                )
                sortOrder.value =
                    if (SortOrder.ASCENDING == sortOrder.value) SortOrder.DESCENDING else SortOrder.ASCENDING
            }) {
                Text(text = "Sort by release")
            }
        }
    }
}

enum class FilterButton {
    NAME, RATING, RELEASE_DATE
}

@Composable
private fun MovieDetailDialog(
    showDialog: MutableState<Boolean>,
    selectedMovie: MutableState<MovieUiState?>,
    action: (HomeEvent) -> Unit
) {
    if (showDialog.value) {
        Dialog(onDismissRequest = {
            showDialog.value = false
            selectedMovie.value = null
        }) {
            selectedMovie.value?.let {
                MovieDetailedComponent(movie = it, onFavoriteClick = { id ->
                    action.invoke(
                        HomeEvent.ChangeFavoriteState(movieId = id)
                    )
                })
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    val uiState = HomeUiState(
        movieList = listOf(
        ), favoriteList = listOf(
            MovieUiState(
                movieId = 2823,
                movieTitle = "lorem",
                movieOverview = "blandit",
                posterPath = "molestie",
                releaseDate = "mutat",
                rating = 14.15,
                isFavorite = false
            ),
            MovieUiState(
                movieId = 2823,
                movieTitle = "lorem",
                movieOverview = "blandit",
                posterPath = "molestie",
                releaseDate = "mutat",
                rating = 14.15,
                isFavorite = false
            ),
        )
    )
    HomeScreen(uiState = uiState, isLoading = true) {

    }
}