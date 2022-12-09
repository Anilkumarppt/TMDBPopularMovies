package com.anil.tmdbpopularmovies.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_casts")
data class CastDto(
    var adult: Boolean,
    @PrimaryKey
    var castId: Int,
    var character: String,
    var gender: Int,
    var id: Int,
    var knownForDepartment: String,
    var name: String,
    var originalName: String,
    var profilePath: String,
    var popularity: Double,
)
