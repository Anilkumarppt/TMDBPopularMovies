package com.anil.tmdbpopularmovies.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSourceImpl
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSourceImpl
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface MovieDataSource  {
    @ExperimentalPagingApi
    suspend fun getPagedMovies(moviesRemoteMediator:  MovieRemoteMediator): Flow<PagingData<Movie>>
    suspend fun getPagedMovies(page:Int): Response<MovieList>
}