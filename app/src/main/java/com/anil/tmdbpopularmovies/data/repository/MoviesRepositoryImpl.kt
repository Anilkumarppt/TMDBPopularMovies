package com.anil.tmdbpopularmovies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.apiservice.MoviesAPIService
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val db: MoviesDatabase
) : MoviesRepository {

    @ExperimentalPagingApi
    val moviesRemoteMediator = MovieRemoteMediator(
        movieLocalDataSource = movieLocalDataSource,
        movieRemoteDataSource = movieRemoteDataSource,
        db = db
    )

    override suspend fun getPagedMovies(page: Int): Response<MovieList> {
        //delay(3000)
        TODO("Not yet implemented")
        //return moviesAPIService.getPagingPopularMovies(page = page)
    }

    override suspend fun insertPagedMovies(movies: List<Movie>) {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
       return movieLocalDataSource.getMovieById(movieId = movieId)
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMostPopularMovies(): Flow<PagingData<Movie>> {
        return movieLocalDataSource.getPagedMovies(moviesRemoteMediator = moviesRemoteMediator)


        TODO("Not yet implemented")
    }

}