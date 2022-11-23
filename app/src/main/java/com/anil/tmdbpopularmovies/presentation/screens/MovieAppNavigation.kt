package com.anil.tmdbpopularmovies.presentation.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anil.tmdbpopularmovies.presentation.screens.details.MovieDetailScreen
import com.anil.tmdbpopularmovies.presentation.screens.details.MovieDetailsViewModel
import com.anil.tmdbpopularmovies.presentation.screens.movielist.MovieListViewModel
import com.anil.tmdbpopularmovies.presentation.screens.movielist.MoviesListScreen

@ExperimentalMaterialApi
@Composable
fun MovieApp() {
    val navController = rememberNavController()

    val listViewModel: MovieListViewModel = viewModel()
    val detailViewModel: MovieDetailsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = MovieAppScreen.MainScreen.route
    ) {
        composable(route = MovieAppScreen.MainScreen.route) {
            MoviesListScreen(navController = navController, viewModel = listViewModel)
        }
        composable(
            route = MovieAppScreen.DetailScreen.route + "/{movie_id}",
            arguments = listOf(
                navArgument("movie_id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            MovieDetailScreen(
                movieId = entry.arguments?.getInt("movie_id"),
                navController = navController,
                movieDetailsViewModel =detailViewModel
            )
        }
    }
}