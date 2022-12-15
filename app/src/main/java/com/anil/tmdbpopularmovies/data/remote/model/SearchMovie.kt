package com.anil.tmdbpopularmovies.data.remote.model

import androidx.core.app.NotificationCompat
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.google.gson.annotations.SerializedName

data class SearchMovie(
    @SerializedName("page")
     val page: Int,
    @SerializedName("results")
     val searchMovieResults: List<MovieDto>
)
