package com.anil.tmdbpopularmovies.data.remote.apiservice

import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.remote.model.Cast
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.util.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPIService {

    @GET(AppConstants.GET_POPULAR)
    suspend fun getPagingPopularMovies(
        @Query("page") page: Int
    ): Response<MovieList>

    @GET(AppConstants.MOVIE)
    suspend fun getFullMovieData(@Path("movie_id") movieId: Int): Movie

    @GET(AppConstants.CREDITS)
    suspend fun getMovieCasts(@Path("movie_id") movieId:Int):Cast

}