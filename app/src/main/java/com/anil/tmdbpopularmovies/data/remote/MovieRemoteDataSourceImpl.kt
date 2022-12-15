package com.anil.tmdbpopularmovies.data.remote

import android.util.Log
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.remote.apiservice.MoviesAPIService
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.SearchMovie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMoviesList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


class MovieRemoteDataSourceImpl @Inject constructor(private val moviesAPIService: MoviesAPIService) :MovieRemoteDataSource {

    override suspend fun addFreshPopularMovies(movies: List<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshMovies(popularMovies: List<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun getPagedPopularMovies(page: Int): Response<MovieList> {

        /* val response = moviesAPIService.getPagingPopularMovies(page).body()!!
        val currentPage = response.page
        response.results.map {
            it.toDomain(page = currentPage)
        }.sortedByDescending { page }*/
        return moviesAPIService.getPagingPopularMovies(page)
    }

    override suspend fun getTopRatedMoviesList(page: Int): Response<TopRatedMoviesList> {
        return moviesAPIService.getTopRatedMovies(page = page)
    }

    override suspend fun getSearchMovieByTitle(title: String): Response<SearchMovie> {
        return moviesAPIService.searchMovieByTitle(title)
    }

}
