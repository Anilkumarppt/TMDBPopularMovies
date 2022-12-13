package com.anil.tmdbpopularmovies.data.local

import android.util.Log
import androidx.paging.*
import com.anil.tmdbpopularmovies.data.local.dao.MovieDao
import com.anil.tmdbpopularmovies.data.local.dao.TopRatedMovieDao
import com.anil.tmdbpopularmovies.data.local.dto.CastDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.paging.TopRatedMovieRemoteMeditor
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
private const val PAGE_SIZE = 20
class MovieLocalDataSourceImpl @Inject constructor(private val movieDao: MovieDao,private val topRatedMovieDao: TopRatedMovieDao) :
    MovieLocalDataSource {

    override suspend fun addFreshPopularMovies(movies: List<Movie>) {
        movies.map { movie ->
            movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
        }
        movieDao.savePopularMovies(movies)
    }

    override suspend fun getMovieById(movieId: Int): Flow<Movie?> {
        val fullMovieData = movieDao.getFullMovieData(movieId = movieId)
        Log.d("Local DataBase", "getMovieById: ${fullMovieData.toString()}")
        return fullMovieData
    }

    override suspend fun refreshMovies(popularMovies: List<Movie>) {
        TODO("Not yet implemented")
    }

    @ExperimentalPagingApi
    override suspend fun getPagedMovies(moviesRemoteMediator: MovieRemoteMediator): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = moviesRemoteMediator,
            pagingSourceFactory = movieDao::getMoviesPagingSource,
        ).flow

    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getTopRatedMovies(moviesRemoteMediator: TopRatedMovieRemoteMeditor): Flow<PagingData<TopRatedMovieDto>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE,
                enablePlaceholders = false),
            remoteMediator = moviesRemoteMediator,
            pagingSourceFactory = {
                topRatedMovieDao.getTopRatedMoviesPagingSource()
            }
        ).flow
    }

    override suspend fun getLocalPagedMovies(): List<Movie> {
        return movieDao.getPopularMoviesFromDB()
    }

    override suspend fun countMovies(): Int {
        return movieDao.countMovies()
    }

    override suspend fun getMovieCasts(): Flow<List<CastDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCasts(castList: List<CastDto>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertTopRatedMovies(movies: List<TopRatedMovieDto>) {
        TODO("Not yet implemented")
    }


}