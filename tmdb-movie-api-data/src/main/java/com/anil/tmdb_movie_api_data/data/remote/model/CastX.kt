package com.anil.tmdb_movie_api_data.data.remote.model


import com.google.gson.annotations.SerializedName

data class CastX(
    @SerializedName("adult")
    var adult: Boolean,
    @SerializedName("cast_id")
    var castId: Int,
    @SerializedName("character")
    var character: String,
    @SerializedName("credit_id")
    var creditId: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("known_for_department")
    var knownForDepartment: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("order")
    var order: Int,
    @SerializedName("original_name")
    var originalName: String,
    @SerializedName("popularity")
    var popularity: Double,
    @SerializedName("profile_path")
    var profilePath: String
)