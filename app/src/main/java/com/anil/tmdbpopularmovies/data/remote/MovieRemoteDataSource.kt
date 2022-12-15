package com.anil.tmdbpopularmovies.data.remote

import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.SearchMovie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMoviesList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRemoteDataSource {

    suspend fun addFreshPopularMovies(movies: List<Movie>)
    suspend fun getMovieById(movieId: Int): Flow<Movie?>
    suspend fun refreshMovies(popularMovies: List<Movie>)
    suspend fun getPagedPopularMovies(page:Int): Response<MovieList>
    suspend fun getTopRatedMoviesList(page:Int):Response<TopRatedMoviesList>
    suspend fun getSearchMovieByTitle(title:String):Response<SearchMovie>
}