package com.anil.tmdbpopularmovies.domain.di

import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import com.anil.tmdbpopularmovies.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideBaseMovieUse(moviesRepository: MoviesRepository):BaseUseCase= BaseUseCase(repository = moviesRepository)
    @Provides
    @Singleton
    fun provideGetMoviesUseCase(moviesRepository: MoviesRepository): GetMoviesUseCase =
        GetMoviesUseCase(moviesRepository = moviesRepository)

    @Provides
    @Singleton
    fun provideGetMoviesDetailUseCase(moviesRepository: MoviesRepository): MovieDetailUseCase =
        MovieDetailUseCase(moviesRepository = moviesRepository)

    @Provides
    @Singleton
    fun provideGetTopMoviesUseCase(moviesRepository: MoviesRepository): GetTopRatedMovieUseCase =
        GetTopRatedMovieUseCase(moviesRepository = moviesRepository)

    @Provides
    @Singleton
    fun provideGetSearchTextUseCase(moviesRepository: MoviesRepository):GetSearchMovieUse=
        GetSearchMovieUse(moviesRepository = moviesRepository)
}