package com.anil.tmdbpopularmovies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.paging.MoviePagingSource
import com.anil.tmdbpopularmovies.data.paging.MovieRemoteMediator
import com.anil.tmdbpopularmovies.data.paging.TopRatedMovieRemoteMeditor
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.apiservice.MoviesAPIService
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.*
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
    private val topRatedMovieRemoteMeditor=TopRatedMovieRemoteMeditor(
        movieRemoteDataSource = movieRemoteDataSource,
        db=db)



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
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getTopRatedMovies(): Flow<PagingData<TopRatedMovieDto>> {
        return movieLocalDataSource.getTopRatedMovies(moviesRemoteMediator = topRatedMovieRemoteMeditor)
        //TODO("Not yet implemented")
    }

    override suspend fun insertTopRatedMovies(movies: List<TopRatedMovieDto>) {

    }

    override suspend fun getTopRatedMovieById(movieId: Int): Flow<TopRatedMovieDto> {
        TODO("Not yet implemented")
    }

    /* fetch the search result based on title parameter
    * */
    override suspend fun getSearchMovieByTitle(title: String): Flow<PagingData<MovieDto>> {
        val filterFlow = MutableStateFlow<String>("")
        filterFlow.value=title
        var latestFlow: Flow<PagingData<MovieDto>>
        latestFlow=Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {MoviePagingSource(
                remoteDataSource = movieRemoteDataSource,
                searchKey = title
            )}
        ).flow
        /*filterFlow.collectLatest {
            latestFlow=Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = true,
                    initialLoadSize = 1
                ),
                pagingSourceFactory = {MoviePagingSource(
                    remoteDataSource = movieRemoteDataSource,
                    searchKey = it
                )}
            ).flow
        }*/
        return latestFlow
    }

}