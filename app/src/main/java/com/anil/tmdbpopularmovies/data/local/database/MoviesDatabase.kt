package com.anil.tmdbpopularmovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anil.tmdbpopularmovies.data.local.MovieDao
import com.anil.tmdbpopularmovies.data.remote.model.Movie

@Database(version = 1, entities = [Movie::class,RemoteKey::class])
abstract class MoviesDatabase :RoomDatabase(){
    abstract fun getMovieDao():MovieDao
    abstract fun getRemoteKeysDao():RemoteKeyDao
}