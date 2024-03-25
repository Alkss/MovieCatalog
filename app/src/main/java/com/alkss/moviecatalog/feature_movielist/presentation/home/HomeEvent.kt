package com.alkss.moviecatalog.feature_movielist.presentation.home

sealed class HomeEvent {
    data object ForceRefresh : HomeEvent()

    data object NextPage : HomeEvent()
    data class ChangeFavoriteState(val movieId: Int) : HomeEvent()
    data class SortMovieList(val sortType: SortType, val sortOrder: SortOrder = SortOrder.DESCENDING) : HomeEvent()
}

enum class SortType {
    NAME,
    RELEASE_DATE,
    RATING
}

enum class SortOrder {
    ASCENDING,
    DESCENDING
}
