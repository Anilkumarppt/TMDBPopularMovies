package com.anil.tmdbpopularmovies.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.presentation.MovieListViewModel
import com.anil.tmdbpopularmovies.presentation.composebles.ErrorItem
import com.anil.tmdbpopularmovies.presentation.composebles.LoadingIndicator
import com.anil.tmdbpopularmovies.presentation.composebles.PosterImage
import com.anil.tmdbpopularmovies.ui.theme.*
import kotlinx.coroutines.CoroutineScope

@Composable
fun MoviesListScreen() {
    val viewModel: MovieListViewModel = viewModel()

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val snackBarScope: CoroutineScope = rememberCoroutineScope()

    val movieItems = viewModel.movieList.collectAsLazyPagingItems()


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
            moviesViewModel = viewModel
        )
    }
    //LoadingIndicator()

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
    moviesViewModel: MovieListViewModel
) {

    val movieItems = moviesViewModel.movieList.collectAsLazyPagingItems()
    //val savedListState = rememberLazyListState(scrollingListPosition)
    Column(
        modifier = modifier
            .background(Purple200)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    )

    {
        MovieScreenTopPart()

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .fillMaxHeight()
                .background(md_theme_light_secondary),
            Arrangement.SpaceBetween
        ) {
            RecommendedMoviesList(movieItems, scaffoldState, snackbarScope)
            RecommendedMoviesList(movieItems, scaffoldState, snackbarScope)

        }
        /*Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(md_theme_light_secondary)
                .height(320.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            HeadingText(text1 = "Recommended", text2 ="See all" )
            Spacer(modifier = Modifier.height(12.dp))
            LazyVerticalGrid(cells = GridCells.Fixed(2),
                content = {
                    items(movieItems.itemCount) { index ->
                        movieItems[index]?.let {
                            MovieItem(
                                scaffoldState = scaffoldState,
                                scope = snackbarScope,
                                movie = movieItems[index]!!
                            )
                        }
                    }
                })
        }*/
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendedMoviesList(
    movieItems: LazyPagingItems<Movie>,
    scaffoldState: ScaffoldState,
    snackbarScope: CoroutineScope
) {
    Column(modifier = Modifier.height(320.dp),
        verticalArrangement = Arrangement.SpaceAround) {
        HeadingText(text1 = "Recommended", text2 = "See all")
        LazyRow(content = {
            items(movieItems){movie->
                MovieItem(
                    scaffoldState = scaffoldState,
                    scope = snackbarScope,
                    movie = movie!!
                )
            }
            }, contentPadding = PaddingValues(5.dp))
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
    fun HeadingText(text1: String, text2: String) {
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
                textAlign = TextAlign.End
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
                HeadingText(text1 = "Recommended", text2 = "See all")
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
