package com.anil.tmdbpopularmovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anil.tmdbpopularmovies.data.local.dao.CastDao
import com.anil.tmdbpopularmovies.data.local.dao.MovieDao
import com.anil.tmdbpopularmovies.data.local.dto.CastDto
import com.anil.tmdbpopularmovies.data.remote.model.Movie

@Database(version = 2, entities = [Movie::class,RemoteKey::class,CastDto::class])
abstract class MoviesDatabase :RoomDatabase(){
    abstract fun getMovieDao(): MovieDao
    abstract fun getRemoteKeysDao():RemoteKeyDao
    abstract fun getCastsDao(): CastDao

}