package com.anil.tmdbpopularmovies.data.remote.model


import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("cast")
    var cast: List<CastX>,
    @SerializedName("crew")
    var crew: List<Crew>,
    @SerializedName("id")
    var id: Int
)