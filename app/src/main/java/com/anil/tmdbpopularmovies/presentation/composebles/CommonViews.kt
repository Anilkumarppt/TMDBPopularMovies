package com.anil.tmdbpopularmovies.presentation.composebles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.anil.tmdbpopularmovies.R
import com.anil.tmdbpopularmovies.ui.theme.graySurface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoadingIndicator() {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview
fun DisplayPreview() {
    ErrorItem(message = "Some Error Occured")
}

@Composable
fun ErrorItem(message: String) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp),
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                color = Color.Black,
                text = message,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun FavoriteIcon(
    modifier: Modifier = Modifier,
    color: Color=Color(0xffE91E63)
) {
    var isFavorite by remember {
        mutableStateOf(false)
    }

    IconToggleButton(checked = isFavorite, onCheckedChange = {
        isFavorite = !isFavorite
    })
    {
        Icon(
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            }, contentDescription = null, tint = color
        )
    }
}

@ExperimentalCoilApi
@Composable
fun PosterImage(
    poster: String?,
    title: String?,
    movieId: Int,
    scrollId: Int,
    onPosterClick: (Int, Int) -> Unit
) {
    val posterPainter = rememberImagePainter(
        data = poster,
        builder = {
            crossfade(true)
            scale(Scale.FILL)
        })
    Box(
    ) {
        Image(
            painter = posterPainter,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onPosterClick(movieId, scrollId)
                })
        )
        /*Spacer(modifier = Modifier.height(16.dp))
            Text(
                title!!,
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.body1,
                color = Color.White
          )*/
    }
    val imageState = posterPainter.state
    if (imageState is ImagePainter.State.Loading) {
        Box(
            Modifier
                .height(278.dp)
                .width(185.dp),
            Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
    if (imageState is ImagePainter.State.Error) {
        Box(
            Modifier
                .height(278.dp)
                .width(185.dp)
                .border(
                    1.dp, MaterialTheme.colors.secondary.copy(alpha = 0.1F)
                ),
            Alignment.Center
        ) {
            Icon(
                Icons.Default.MovieCreation,
                title,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center),
                tint = Color.Black.copy(alpha = 0.2F)
            )
            if (title != null) {
                Text(
                    title,
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }

}

@Composable
fun TMDTopAppBar(scaffoldState: ScaffoldState, snackBarScope: CoroutineScope) {

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        backgroundColor = graySurface,
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Menu"
                )

            }
        },
        actions = {
            IconButton(onClick = {
                snackBarScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("TODO implement search")
                }
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search movies")
            }
        },
    )
}

