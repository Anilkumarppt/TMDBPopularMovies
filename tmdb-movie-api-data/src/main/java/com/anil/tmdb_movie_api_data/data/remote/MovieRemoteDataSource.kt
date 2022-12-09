package com.anil.tmdb_movie_api_data.data.remote

import com.anil.tmdb_movie_api_data.data.remote.model.MovieList
import com.anil.tmdb_movie_api_data.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRemoteDataSource {

    suspend fun addFreshPopularMovies(movies: List<Movie>)
    suspend fun getMovieById(movieId: Int): Flow<Movie?>
    suspend fun refreshMovies(popularMovies: List<Movie>)
    suspend fun getPagedPopularMovies(page:Int): Response<MovieList>

}