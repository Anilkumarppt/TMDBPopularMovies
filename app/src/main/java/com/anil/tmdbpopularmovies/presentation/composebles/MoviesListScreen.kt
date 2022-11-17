package com.anil.tmdbpopularmovies.presentation.composebles

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.presentation.MovieListViewModel
import com.anil.tmdbpopularmovies.ui.theme.Purple200
import com.anil.tmdbpopularmovies.ui.theme.md_theme_dark_surface
import com.anil.tmdbpopularmovies.ui.theme.md_theme_light_background
import com.anil.tmdbpopularmovies.ui.theme.md_theme_light_onBackground
import kotlinx.coroutines.CoroutineScope

@Composable
fun MoviesListScreen() {
    val viewModel: MovieListViewModel = viewModel()

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val snackBarScope: CoroutineScope = rememberCoroutineScope()

    val movieItems = viewModel.movieList.collectAsLazyPagingItems()
    Log.d("MyTag", "Greeting: ${movieItems.itemCount}")

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TMDTopAppBar(scaffoldState = scaffoldState, snackBarScope = snackBarScope)
        },
        backgroundColor = md_theme_light_onBackground
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
    }

}

@Composable
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
}

@Composable
fun MovieItem(scaffoldState: ScaffoldState, scope: CoroutineScope, movie: Movie) {
    Card(
        modifier = Modifier
            .height(278.dp)
            .width(185.dp),
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
