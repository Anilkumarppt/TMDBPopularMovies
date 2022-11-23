package com.anil.tmdbpopularmovies.data.local.dto

import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val movie_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val poster: String,
    @SerializedName("backdrop_path") val backdrop: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int

) {
    fun toDomain(page: Int) = Movie(
        id = movie_id,
        title = title,
        posterPath = poster,
        overview = overview,
        releaseDate = release_date,
        favorite = 0,
        video = false,
        page = page,
        voteAverage = voteAverage,
        voteCount = voteCount,
        backdrop = backdrop

    )
}