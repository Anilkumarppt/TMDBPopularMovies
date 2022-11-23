package com.anil.tmdbpopularmovies.presentation.screens

import com.anil.tmdbpopularmovies.data.remote.model.Movie

sealed class MovieAppScreen(val route:String){

    object MainScreen:MovieAppScreen("main_screen")
    object DetailScreen:MovieAppScreen("detail_screen")

    fun withArgs(vararg args:Int):String{

        return buildString {
            append(route)
            args.forEach {
                arg->append("/$arg")
            }
        }

    }

}
