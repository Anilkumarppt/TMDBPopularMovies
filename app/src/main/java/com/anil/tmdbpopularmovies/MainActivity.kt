package com.anil.tmdbpopularmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import com.anil.tmdbpopularmovies.presentation.screens.MovieApp
import com.anil.tmdbpopularmovies.presentation.screens.movielist.MoviesListScreen
import com.anil.tmdbpopularmovies.ui.theme.TMDBPopularMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ToastMessage.toastCustom(context = applicationContext,"sample library module")
        setContent {
            TMDBPopularMoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray
                ) {
                   MovieApp()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    TMDBPopularMoviesTheme {
        Greeting("Android")
    }
}