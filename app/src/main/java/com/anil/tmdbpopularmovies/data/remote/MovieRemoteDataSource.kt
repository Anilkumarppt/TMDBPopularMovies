package com.anil.tmdbpopularmovies.data.remote

import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRemoteDataSource {

    suspend fun addFreshPopularMovies(movies: List<Movie>)
    suspend fun getMovieById(movieId: Int): Flow<Movie?>
    suspend fun refreshMovies(popularMovies: List<Movie>)
    suspend fun getPagedPopularMovies(page:Int): Response<MovieList>

}