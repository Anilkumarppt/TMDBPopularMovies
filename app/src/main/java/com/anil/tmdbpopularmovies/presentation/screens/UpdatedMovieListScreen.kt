package com.anil.tmdbpopularmovies.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.anil.tmdbpopularmovies.R
import com.anil.tmdbpopularmovies.presentation.MovieListViewModel
import com.anil.tmdbpopularmovies.ui.theme.Purple200
import com.anil.tmdbpopularmovies.ui.theme.lightGray
import com.anil.tmdbpopularmovies.ui.theme.md_theme_light_background
import kotlinx.coroutines.CoroutineScope

@Composable
fun UpdatedMovieList() {
    val viewModel: MovieListViewModel = viewModel()

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val snackBarScope: CoroutineScope = rememberCoroutineScope()

    val movieItems = viewModel.movieList.collectAsLazyPagingItems()


}


@Composable
fun MovieScreenTopPart() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(
                Purple200
            )
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.width(250.dp)) {
            Text(
                text = "Hello, what do you want to watch?",
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Start,
                lineHeight = 35.sp,
                maxLines = 2,
                color = Color.White,
                fontWeight = Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SearchViewTextField()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun SearchInputField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    TextField(value = text, onValueChange = { newText ->
        text = newText
    })
}


@Composable
fun SearchViewTextField() {
    val searchBy = remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = CircleShape
            )
            .width(300.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = searchBy.value,
            onValueChange = {
                searchBy.value = it
            },

            modifier = Modifier
                .background(lightGray, CircleShape)
                .height(30.dp)
                .fillMaxWidth(),
            singleLine = true,
            maxLines = 1,

            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "image",
                        tint = Color.LightGray
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchBy.value.equals(TextFieldValue(""))) Text(
                            "Search", color = Color.LightGray
                        )
                        innerTextField()
                    }

                    if (!searchBy.value.equals(TextFieldValue(""))) {
                        IconButton(
                            onClick = {
                                searchBy.value = TextFieldValue("")
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "image",
                                tint = Color.LightGray
                            )
                        }
                    }
                }
            }

        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewMovieScreen() {
    MovieScreenTopPart()
    //SearchInputField()
}