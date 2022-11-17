package com.anil.tmdbpopularmovies.data.remote.apiservice

import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.util.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesAPIService {

    @GET(AppConstants.GET_POPULAR)
    suspend fun getPagingPopularMovies(
        @Query("page") page: Int
    ): Response<MovieList>
}