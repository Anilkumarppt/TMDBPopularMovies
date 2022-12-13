package com.anil.tmdbpopularmovies.presentation.screens.movielist

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.presentation.composebles.ErrorItem
import com.anil.tmdbpopularmovies.presentation.composebles.LoadingIndicator
import com.anil.tmdbpopularmovies.presentation.composebles.PosterImage
import com.anil.tmdbpopularmovies.presentation.screens.MovieAppScreen
import com.anil.tmdbpopularmovies.presentation.screens.MovieCard
import com.anil.tmdbpopularmovies.presentation.screens.MovieScreenTopPart
import com.anil.tmdbpopularmovies.ui.theme.*
import kotlinx.coroutines.CoroutineScope

private const val TAG = "MoviesListScreen"
@Composable
fun MoviesListScreen(navController: NavController,viewModel:MovieListViewModel) {



    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val snackBarScope: CoroutineScope = rememberCoroutineScope()

    //val movieItems = viewModel.movieList.collectAsLazyPagingItems()

    Scaffold(
        scaffoldState = scaffoldState,
        /*topBar = {
            TMDTopAppBar(scaffoldState = scaffoldState, snackBarScope = snackBarScope)
        },*/
        backgroundColor = graySurface
    ) {
        MoviesListScreenContent(
            modifier = Modifier.padding(it),
            scaffoldState = scaffoldState,
            snackbarScope = snackBarScope,
            moviesViewModel = viewModel,
            navController
        )
    }
    //LoadingIndicator()


}

@Composable
fun LoadingStates(movieItems: LazyPagingItems<Movie>) {
    when (movieItems.loadState.append) {
        is LoadState.NotLoading -> Unit
        LoadState.Loading -> {
            LoadingIndicator()
        }
        is LoadState.Error -> {
            ErrorItem(message = "Some error occurred")
        }
    }
    when (movieItems.loadState.refresh) {
        is LoadState.NotLoading -> Unit
        LoadState.Loading -> {
            LoadingIndicator()
        }
        is LoadState.Error -> ErrorItem(message = "Some error occured")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesListScreenContent(
    modifier: Modifier,
    scaffoldState: ScaffoldState,
    snackbarScope: CoroutineScope,
    moviesViewModel: MovieListViewModel,
    navController: NavController,
) {
    val movieItems = moviesViewModel.movieList.collectAsLazyPagingItems()
    val topRatedMovies=moviesViewModel.topRatedMoviesList.collectAsLazyPagingItems()
    //val savedListState = rememberLazyListState(scrollingListPosition)
    Column(
        modifier = modifier
            .background(Purple200)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    )

    {
        MovieScreenTopPart()
        Spacer(modifier = Modifier.height(28.dp))
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp
                    )
                )
                .fillMaxHeight()
                .background(md_theme_dark_onSecondary)
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //LoadingStates(movieItems = movieItems)
            var clickevent by rememberSaveable { mutableStateOf(0) }
            Column(modifier = Modifier.weight(1F)) {
                RecommendedMoviesList(
                    movieItems,
                    scaffoldState,
                    snackbarScope,
                    1,
                    onPosterClick = { movieid ->
                        Log.d(TAG, "MoviesListScreenContent: $movieid")
                        navController.navigate(MovieAppScreen.DetailScreen.withArgs(movieid))
                    })
            }
            Column(modifier = Modifier.weight(1F)) {
                TopRatedMoviesList(
                    topRatedMovies,
                    scaffoldState,
                    snackbarScope,
                    2,
                    onPosterClick = { movieid ->
                        navController.navigate(MovieAppScreen.DetailScreen.withArgs(movieid))
                    })
                //LoadingStates(movieItems = movieItems)
            }


        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendedMoviesList(
    movieItems: LazyPagingItems<Movie>,
    scaffoldState: ScaffoldState,
    snackbarScope: CoroutineScope,
    clickEvent: Int,
    onPosterClick: (Int) -> Unit

) {
    Column(
        modifier = Modifier.height(300.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadingText(text1 = "Recommended for you", text2 = "See all", click = { click ->
                if (click == 1) {
                    Toast.makeText(context, "Click Event of Recommended Movies", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "Click Event of Popular Movies", Toast.LENGTH_LONG)
                        .show()

                }
            }, clickEvent = clickEvent)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyRow(content = {
                items(movieItems) { movie ->
                    MovieCard(
                        posterPath = movie!!.posterPath!!,
                        movieTitle = movie!!.title,
                        rating = movie.voteAverage,
                        movieId = movie.id,
                        onPosterClick = {
                            onPosterClick(it)
                        }
                    )
                }
            }, contentPadding = PaddingValues(5.dp))
        }

    }

}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopRatedMoviesList(
    movieItems: LazyPagingItems<TopRatedMovieDto>,
    scaffoldState: ScaffoldState,
    snackbarScope: CoroutineScope,
    clickEvent: Int,
    onPosterClick: (Int) -> Unit

) {
    Column(
        modifier = Modifier.height(300.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadingText(text1 = "Recommended for you", text2 = "See all", click = { click ->
                if (click == 1) {
                    Toast.makeText(context, "Click Event of Recommended Movies", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "Click Event of Popular Movies", Toast.LENGTH_LONG)
                        .show()

                }
            }, clickEvent = clickEvent)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyRow(content = {
                items(movieItems) { movie ->
                    MovieCard(
                        posterPath = movie!!.posterPath!!,
                        movieTitle = movie!!.title,
                        rating = movie.voteAverage,
                        movieId = movie.id,
                        onPosterClick = {
                            onPosterClick(it)
                        }
                    )
                }
            }, contentPadding = PaddingValues(5.dp))
        }

    }

}
/*@Composable
fun MovieListItem(modifier: Modifier) {
    Column(
        modifier = modifier
            .background(md_theme_dark_surface)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "POPULAR",
                style = MaterialTheme.typography.button,
                fontSize = 10.sp,
                color = Purple200,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "SEE ALL",
                style = MaterialTheme.typography.button,
                fontSize = 10.sp,
                color = Purple200,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }


    }
}*/

@Composable
fun HeadingText(text1: String, text2: String, click: (Int) -> Unit, clickEvent: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = text1.uppercase(),
            color = Color.LightGray,
            style = MaterialTheme.typography.button,
            fontSize = 12.sp,
            fontWeight = Bold,
            textAlign = TextAlign.Start
        )
        Text(
            text = text2,
            color = Color.LightGray,
            fontSize = 12.sp,
            style = MaterialTheme.typography.button,
            textAlign = TextAlign.End,
            modifier = Modifier.clickable { click(clickEvent) }
        )

    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ListScreenPreview() {

    Surface(modifier = Modifier.fillMaxHeight()) {
        MovieScreenTopPart()

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(md_theme_light_secondary)
        ) {
            Spacer(modifier = Modifier.height(22.dp))
            HeadingText(text1 = "Recommended", text2 = "See all", click = { }, 1)
        }
    }


}

@Composable
fun MovieItem(scaffoldState: ScaffoldState, scope: CoroutineScope, movie: Movie) {
    Card(
        modifier = Modifier
            .height(220.dp)
            .width(155.dp),
        elevation = 10.dp,
        backgroundColor = md_theme_light_background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PosterImage(
                poster = movie.posterPath,
                title = movie.title,
                movieId = movie.id,
                scrollId = 1,
                onPosterClick = { movieId, scroll -> })
            Column(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    movie.title!!,
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black, maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }

}
