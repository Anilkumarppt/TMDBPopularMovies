package com.anil.tmdb_movie_api_data.data.remote.model


import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("cast")
    var cast: List<CastX>,
    @SerializedName("crew")
    var crew: List<Crew>,
    @SerializedName("id")
    var id: Int
)