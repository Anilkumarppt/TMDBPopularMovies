package com.anil.tmdbpopularmovies.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
