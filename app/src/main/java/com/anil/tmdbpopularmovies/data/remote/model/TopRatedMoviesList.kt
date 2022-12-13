package com.anil.tmdbpopularmovies.data.remote.model


import com.google.gson.annotations.SerializedName

data class TopRatedMoviesList(
    @SerializedName("page")
    var page: Int,
    @SerializedName("results")
    var results: List<TopRatedMovie>,
    @SerializedName("total_pages")
    var totalPages: Int,
    @SerializedName("total_results")
    var totalResults: Int
)