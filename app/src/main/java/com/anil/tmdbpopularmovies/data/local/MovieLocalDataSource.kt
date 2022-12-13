package com.anil.tmdbpopularmovies.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.dto.CastDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.paging.TopRatedMovieRemoteMeditor
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {

    suspend fun addFreshPopularMovies(movies: List<Movie>)
    suspend fun getMovieById(movieId: Int): Flow<Movie?>
    suspend fun refreshMovies(popularMovies: List<Movie>)

    @ExperimentalPagingApi
    suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<Movie>>

    suspend fun getTopRatedMovies(moviesRemoteMediator: TopRatedMovieRemoteMeditor):Flow<PagingData<TopRatedMovieDto>>

    suspend fun getLocalPagedMovies(): List<Movie>

    suspend fun countMovies(): Int

    suspend fun getMovieCasts():Flow<List<CastDto>>
    suspend fun insertCasts(castList:List<CastDto>)

    suspend fun insertTopRatedMovies(movies:List<TopRatedMovieDto>)
}