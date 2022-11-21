package com.anil.tmdbpopularmovies.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.anil.tmdbpopularmovies.presentation.composebles.RatingBar
import com.anil.tmdbpopularmovies.ui.theme.md_theme_dark_onSecondary


@Composable
fun MovieCard(
    posterPath: String,
    movieTitle: String,
    rating: Double,
    movieId: Int,
    onPosterClick: (Int) -> Unit
) {

    Column(modifier = Modifier.background(md_theme_dark_onSecondary)) {
        Card(
            shape = RoundedCornerShape(30.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .width(240.dp)
                .padding(5.dp)
        ) {

            val posterPainter = rememberImagePainter(
                data = posterPath,
                builder = {
                    crossfade(true)
                    scale(Scale.FILL)
                })
            Box(
                modifier = Modifier
                    .height(278.dp),
            ) {
                Image(
                    painter = posterPainter,
                    contentDescription = movieTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            onPosterClick(movieId)
                        })
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movieTitle,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.White,
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.padding(start = 6.dp)) {
            RatingBar(rating = 3.5)
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Preview
@Composable
fun PreviewMovieCard() {
    MovieCard(
        posterPath = "",
        movieTitle = "ShanChi",
        rating = 3.4,
        movieId = 1222,
        onPosterClick = {})
}