package com.anil.tmdbpopularmovies.data.local.dto

data class CastDto(
    var adult: Boolean,
    var castId: Int,
    var character: String,
    var gender: Int,
    var id: Int,
    var knownForDepartment: String,
    var name: String,
    var originalName: String,
    var profilePath: String
)
