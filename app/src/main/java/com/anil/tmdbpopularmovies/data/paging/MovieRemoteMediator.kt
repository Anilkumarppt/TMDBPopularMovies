package com.anil.tmdbpopularmovies.data.paging

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.dao.MovieDao
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import com.anil.tmdbpopularmovies.data.local.database.RemoteKey
import com.anil.tmdbpopularmovies.data.local.database.RemoteKeyDao
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.util.AppConstants.STARTING_PAGE_INDEX
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val db: MoviesDatabase,
    private val initialPage: Int = 1
) : RemoteMediator<Int, Movie>() {

    private val TAG: String = "MyTag"
    private val movieDao: MovieDao = db.getMovieDao()
    private val remoteKeyDao: RemoteKeyDao = db.getRemoteKeysDao()
    private var currentPage: Int? = null


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        return prepareLoad(loadType = loadType, state)
    }

    private suspend fun prepareLoad(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {
            /*Preparing the Requesting Page number*/
            val pageRequestNum = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    Log.d(TAG, "sample3: Prepend Called")
                    val remoteKeys = getFirstRemoteKey(state)
                    val prevKey = remoteKeys?.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKey = getLastRemoteKey(state)
                    Log.d(TAG, "sample3: Append Called")
                    //printRemoteKeys(remoteKey)
                    val nextKey = remoteKey?.nextKey
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    }
                    nextKey
                }
            }

            val response = movieRemoteDataSource.getPagedPopularMovies(pageRequestNum)
            currentPage = response.body()!!.page
            val moviesList: List<Movie> = convertMovieList(response.body()!!)
            val endOfPagination = moviesList.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteAllKeys()
                    movieDao.deleteAllMovies()
                }

                val prevKey =
                    if (pageRequestNum == STARTING_PAGE_INDEX) null else pageRequestNum - 1
                val nextKey = if (endOfPagination) null else pageRequestNum.plus(1)
                val keys = moviesList.map { movie ->
                    Log.d("MyTag", "load:  RemoteKeys $prevKey   $nextKey  ${movie.page}")
                    RemoteKey(movieId = movie.page, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAll(keys)
                moviesList.map { movie ->
                    movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
                    movie.backdrop = "https://image.tmdb.org/t/p/w500" + movie.backdrop
                }
                movieDao.savePopularMovies(moviesList)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (ex: Exception) {
            Log.e("MyTag", "sample3:  $ex")
            MediatorResult.Error(ex)
        } catch (ex: HttpException) {
            Log.e("MyTag", "sample3:  $ex")
            MediatorResult.Error(ex)
        }

    }

    private fun convertMovieList(movieListRes: MovieList): List<Movie> {
        if (movieListRes.results.isNotEmpty()) {
            val movies: List<Movie> = movieListRes.results.map {
                it.toDomain(movieListRes.page)
            }.sortedBy { it.page }
            return movies
        }
        return emptyList()
    }

    private suspend fun printRemoteKeys(remoteKey: RemoteKey?) {
        if (remoteKey?.nextKey != null)
            Log.d(TAG, "sample3: Refresh ${remoteKey.nextKey}")
        else
            Log.d(TAG, "sample3: Refresh ${remoteKey?.toString()}")

    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Movie>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> db.getRemoteKeysDao().remoteKeyByMovieId(movie.page) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.getRemoteKeysDao().remoteKeyByMovieId(id)
            }
        }
    }

    /**
     * Get the page of the last Movie item loaded from the database
     * Returns null if no data passed to Mediator
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> db.getRemoteKeysDao().remoteKeyByMovieId(movieId = movie.page) }
    }

}