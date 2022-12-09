package com.anil.tmdbpopularmovies.domain.di

import android.content.Context
import androidx.room.Room
import com.anil.tmdbpopularmovies.data.local.dao.CastDao
import com.anil.tmdbpopularmovies.data.local.dao.MovieDao
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideMovieDB(@ApplicationContext appContext:Context):MoviesDatabase=Room.databaseBuilder(appContext,MoviesDatabase::class.java,"Movies.db").build()

    @Provides
    fun provideMovieListDao(moviesDatabase: MoviesDatabase): MovieDao =moviesDatabase.getMovieDao()

    @Provides
    fun provideMovieCastListDao(moviesDatabase: MoviesDatabase): CastDao =moviesDatabase.getCastsDao()

}