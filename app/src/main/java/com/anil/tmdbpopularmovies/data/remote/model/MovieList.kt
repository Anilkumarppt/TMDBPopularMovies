package com.anil.tmdbclientapp.data.model.movie


import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
){

}