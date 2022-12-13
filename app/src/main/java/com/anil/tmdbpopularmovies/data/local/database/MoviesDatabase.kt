package com.anil.tmdbpopularmovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anil.tmdbpopularmovies.data.local.dao.*
import com.anil.tmdbpopularmovies.data.local.dto.CastDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.local.dto.RemoteKey
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedRemoteKey

@Database(
    version = 2, entities = [Movie::class,
        RemoteKey::class,
        CastDto::class,
        TopRatedMovieDto::class,
        TopRatedRemoteKey::class], exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getRemoteKeysDao(): RemoteKeyDao
    abstract fun getCastsDao(): CastDao
    abstract fun getTopRatedMovieDao(): TopRatedMovieDao
    abstract fun getTopRatedRemoteKeys():TopMovieRemoteKeyDao

}