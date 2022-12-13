package com.anil.tmdb_movie_api_data.data.remote.apiservice

import com.anil.tmdb_movie_api_data.data.AppConstants
import com.anil.tmdb_movie_api_data.data.remote.ResponseObject
import com.anil.tmdb_movie_api_data.data.remote.model.MovieList

import com.anil.tmdb_movie_api_data.data.remote.model.Cast
import com.anil.tmdb_movie_api_data.data.remote.model.Movie

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPIService {

    @GET(AppConstants.GET_POPULAR)
    suspend fun getPagingPopularMovies(
        @Query("page") page: Int
    ): Response<Any>

    @GET(AppConstants.MOVIE)
    suspend fun getFullMovieData(@Path("movie_id") movieId: Int):Result<Any>

    @GET(AppConstants.CREDITS)
    suspend fun getMovieCasts(@Path("movie_id") movieId:Int): Result<Any>

}