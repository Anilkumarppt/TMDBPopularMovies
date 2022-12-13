package com.anil.tmdbpopularmovies.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "top_rated_movie")
data class TopRatedMovieDto(@SerializedName("backdrop_path")
                            var backdropPath: String,

                            @SerializedName("id")
                            @PrimaryKey var id: Int,
                            /*@SerializedName("original_title")
                            var originalTitle: String,*/
                            @SerializedName("overview")
                            var overview: String,
                            @SerializedName("poster_path")
                            var posterPath: String,
                            @SerializedName("release_date")
                            var releaseDate: String,
                            @SerializedName("title")
                            var title: String,
                            @SerializedName("vote_average")
                            var voteAverage: Double,
                            @SerializedName("vote_count")
                            var voteCount: Int,
                            var page:Int){
}