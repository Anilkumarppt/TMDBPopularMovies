package com.anil.tmdbpopularmovies.presentation.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.anil.tmdbpopularmovies.R
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.presentation.composebles.FavoriteIcon
import com.anil.tmdbpopularmovies.presentation.composebles.LoadingIndicator
import com.anil.tmdbpopularmovies.ui.theme.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


private const val TAG = "MovieDetailScreen"


@Composable
fun MovieDetailScreen(
    movieId: Int?,
    navController: NavController?,
    movieDetailsViewModel: MovieDetailsViewModel
) {
    movieDetailsViewModel.getMovieById(movieId)
    val fullMovieData:Movie? by movieDetailsViewModel.movie.collectAsState(initial = null)
    fullMovieData?.let {movie->
        Column(
            modifier = Modifier
                .background(md_theme_dark_onSecondary)
                .fillMaxSize()
        ) {
            Log.d(TAG, "MovieDetailScreen: ${fullMovieData.toString()}")
            //Text(text = "Detail Screen with movie Id $movieId", style = MaterialTheme.typography.h6, textAlign = TextAlign.Center)
            BackDropPoster(backdrop = movie.backdrop, title = movie.title)
            DetailTitleRow(title = movie.title, rating =movie.voteAverage,releaseDate=movie.releaseDate, overView = movie.overview )
            //MovieOverview(overView =movie.overview )
        }
    }

}

@Composable
fun MovieRating(rating: Double, tint:Color) {
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            Icons.Filled.Star,
            contentDescription = "$rating",
            tint = tint
        )
        Text(
            "${rating} / 10",
            Modifier.padding(start = 8.dp),
            color = tint
        )
    }
}
@Composable
fun ReleaseDate(releaseDate: String?, tint: Color) {
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            Icons.Filled.DateRange,
            contentDescription = "$releaseDate",
            tint = tint
        )
        Text(
            "$releaseDate",
            Modifier.padding(start = 8.dp),
            color = tint
        )
    }
}
@Composable
fun DetailTitleRow(title:String,rating:Double,releaseDate:String,overView: String){

    Column() {
        Row(
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                color = md_theme_light_surface,
                maxLines = 2,
                textAlign = TextAlign.Start,
                modifier= Modifier
                    .width(300.dp)
                    .padding(5.dp))

            FavoriteIcon(modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically))
            /*var isFav by remember {
                mutableStateOf(Boolean)
            }

            IconButton(onClick = { isFav }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterVertically)
                )
            }*/

        }


        MovieOverview(overView = "A channel is configured with a capacity, the maximum number of elements that can be buffered. The channel created in callbackFlow has a default capacity of 64 elements. When you try to add a new element to a full channel, send suspends the producer until there's space for the new element, whereas offer does not add the element to the channel and returns false immediately.")
    }

}
@Composable
fun MovieOverview(overView:String){
    Text(
        modifier=Modifier.padding(10.dp),
        text = overView,
        maxLines = 10,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.25.sp,
        color = TextGrayColor)
}
@Composable
fun BackDropPoster(backdrop: String, title: String) {
    val backdropPainter = rememberImagePainter(data = backdrop, builder = { size(OriginalSize) })
    Image(
        painter = backdropPainter,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
    )
    val imageState = backdropPainter.state
    when (imageState) {
        ImagePainter.State.Empty -> {}
        is ImagePainter.State.Loading -> {
            Box(
                Modifier
                    .height(280.dp)
                    .fillMaxWidth(), Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
        is ImagePainter.State.Success -> {}
        is ImagePainter.State.Error -> {
            Box(
                Modifier
                    .height(280.dp)
                    .fillMaxWidth(), Alignment.Center
            ) {
                Icon(
                    Icons.Default.MovieCreation,
                    title,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center),
                    tint = Color.Black.copy(alpha = 0.2F)
                )
            }
        }
    }
}

@Preview(showBackground = true,
        showSystemUi = true)
@Composable
fun PreviewDetailsScreen() {
    Surface(color = md_theme_dark_background) {
        DetailTitleRow(title = "All Quiet on the Western Front and last of the world  ", releaseDate = "22-02-2022", rating =3.5, overView = "" )
    }
}