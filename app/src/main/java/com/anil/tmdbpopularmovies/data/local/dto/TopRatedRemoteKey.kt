package com.anil.tmdbpopularmovies.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_rated_remote_key")
data class TopRatedRemoteKey(
    @PrimaryKey val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
