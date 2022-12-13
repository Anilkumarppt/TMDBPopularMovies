package com.anil.tmdbpopularmovies.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anil.tmdbpopularmovies.data.local.dao.TopMovieRemoteKeyDao
import com.anil.tmdbpopularmovies.data.local.dao.TopRatedMovieDao
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedRemoteKey
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMoviesList
import com.anil.tmdbpopularmovies.util.AppConstants
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TopRatedMovieRemoteMeditor @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val db: MoviesDatabase,
    private val initialPage: Int = 1
):RemoteMediator<Int,TopRatedMovieDto>() {

    private val TAG: String = "MyTag"
    private val movieDao: TopRatedMovieDao = db.getTopRatedMovieDao()
    private val remoteKeyDao: TopMovieRemoteKeyDao = db.getTopRatedRemoteKeys()
    private var currentPage: Int? = null

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopRatedMovieDto>
    ): MediatorResult {
        return prepareLoad(loadType = loadType, state = state)
    }
    private suspend fun prepareLoad(
        loadType: LoadType,
        state: PagingState<Int, TopRatedMovieDto>
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
                    if (prevKey == null)
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
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

            val response = movieRemoteDataSource.getTopRatedMoviesList(pageRequestNum)
            currentPage = response.body()!!.page
            val moviesList: List<TopRatedMovieDto> = convertMovieList(response.body()!!)
            val endOfPagination = moviesList.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteAllTopMovieKeys()
                    movieDao.deleteAllMovies()
                }

                val prevKey =
                    if (pageRequestNum == AppConstants.STARTING_PAGE_INDEX) null else pageRequestNum - 1
                val nextKey = if (endOfPagination) null else pageRequestNum.plus(1)
                val keys = moviesList.map { movie ->
                    //Log.d("MyTag", "load:  RemoteKeys $prevKey   $nextKey  ${movie.page}")
                    TopRatedRemoteKey(movieId = movie.page, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAllTopMovieRemoteKeys(keys)
                moviesList.map { movie ->
                    movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
                    movie.backdropPath = "https://image.tmdb.org/t/p/w500" + movie.backdropPath
                }
                movieDao.saveTopRatedMovies(moviesList)
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

    private fun convertMovieList(movieListRes: TopRatedMoviesList): List<TopRatedMovieDto> {
        if (movieListRes.results.isNotEmpty()) {
            val movies: List<TopRatedMovieDto> = movieListRes.results.map {
               it.toDomain(movieListRes.page)
            }.sortedBy { it.page }
            return movies
        }
        return emptyList()
    }
    private suspend fun getFirstRemoteKey(state: PagingState<Int, TopRatedMovieDto>): TopRatedRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> db.getTopRatedRemoteKeys().remoteKeyByTopMovieId(movie.page) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, TopRatedMovieDto>): TopRatedRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.getTopRatedRemoteKeys().remoteKeyByTopMovieId(id)
            }
        }
    }

    /**
     * Get the page of the last Movie item loaded from the database
     * Returns null if no data passed to Mediator
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, TopRatedMovieDto>): TopRatedRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> db.getTopRatedRemoteKeys().remoteKeyByTopMovieId(movieId = movie.page) }
    }

}
