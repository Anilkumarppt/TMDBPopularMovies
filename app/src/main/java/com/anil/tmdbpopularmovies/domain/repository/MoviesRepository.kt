package com.anil.tmdbpopularmovies.domain.repository

import androidx.paging.PagingData
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesRepository {

    suspend fun getPagedMovies(page:Int): Response<MovieList>

    suspend fun insertPagedMovies(movies: List<Movie>)

    suspend fun getMovieById(movieId:Int):Flow<Movie?>

    suspend fun getMostPopularMovies(): Flow<PagingData<Movie>>

    suspend fun getTopRatedMovies():Flow<PagingData<TopRatedMovieDto>>

    suspend fun insertTopRatedMovies(movies:List<TopRatedMovieDto>)

    suspend fun getTopRatedMovieById(movieId:Int):Flow<TopRatedMovieDto>

    suspend fun getSearchMovieByTitle(title:String):Flow<PagingData<MovieDto>>

}
