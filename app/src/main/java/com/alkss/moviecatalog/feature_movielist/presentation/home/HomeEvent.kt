package com.alkss.moviecatalog.feature_movielist.presentation.home

sealed class HomeEvent {
    data object ForceRefresh : HomeEvent()

    data object NextPage : HomeEvent()
    data class ChangeFavoriteState(val movieId: Int) : HomeEvent()
}
