package com.anil.tmdbpopularmovies.data.local

import androidx.paging.*
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.MovieDataSource
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.remote.apiservice.MoviesAPIService
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
private const val PAGE_SIZE = 20
class MovieLocalDataSourceImpl @Inject constructor(private val movieDao: MovieDao) :
    MovieLocalDataSource {

    override suspend fun addFreshPopularMovies(movies: List<Movie>) {
        movies.map { movie ->
            movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
        }
        movieDao.savePopularMovies(movies)
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshMovies(popularMovies: List<Movie>) {
        TODO("Not yet implemented")
    }

    @ExperimentalPagingApi
    override suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE,
                enablePlaceholders = false),
            remoteMediator = moviesRemoteMediator,
            pagingSourceFactory = {
                movieDao.getMoviesPagingSource()
            }
        ).flow

    }

    override suspend fun getLocalPagedMovies(): List<Movie> {
        return movieDao.getPopularMoviesFromDB()
    }

    override suspend fun countMovies(): Int {
        return movieDao.countMovies()
    }


}