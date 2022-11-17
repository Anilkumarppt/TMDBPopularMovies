package com.anil.tmdbpopularmovies.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

    suspend fun addFreshPopularMovies(movies: List<Movie>)
    suspend fun getMovieById(movieId: Int): Flow<Movie?>
    suspend fun refreshMovies(popularMovies: List<Movie>)

    @ExperimentalPagingApi
    suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<Movie>>

    suspend fun getLocalPagedMovies(): List<Movie>

    suspend fun countMovies(): Int
}