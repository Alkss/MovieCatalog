package com.alkss.moviecatalog.feature_movielist.presentation.util

/**
 * Represents the different screens in the delivery feature.
 *
 * @property route The route associated with the screen.
 */
sealed class Screen(val route: String) {
    /**
     * Represents the home screen.
     */
    data object HomeScreen: Screen("home_screen")

    /**
     * Represents the delivery screen.
     */
    data object FavoriteScreen: Screen("favorite_screen")
}
