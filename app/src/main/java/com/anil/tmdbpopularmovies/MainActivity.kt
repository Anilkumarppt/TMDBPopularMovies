package com.anil.tmdbpopularmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.anil.tmdbpopularmovies.presentation.composebles.MoviesListScreen
import com.anil.tmdbpopularmovies.ui.theme.TMDBPopularMoviesTheme
import com.anil.tmdbpopularmovies.ui.theme.md_theme_dark_primary
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TMDBPopularMoviesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = md_theme_dark_primary
                ) {
                   MoviesListScreen()
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