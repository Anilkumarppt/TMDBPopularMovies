package com.anil.tmdbpopularmovies.data.paging

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.MovieDao
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import com.anil.tmdbpopularmovies.data.local.database.RemoteKey
import com.anil.tmdbpopularmovies.data.local.database.RemoteKeyDao
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.util.AppConstants.ENDING_PAGE_INDEX
import com.anil.tmdbpopularmovies.util.AppConstants.NUM_PAGES_IN_CACHE
import com.anil.tmdbpopularmovies.util.AppConstants.NUM_RESULTS_PER_PAGE
import com.anil.tmdbpopularmovies.util.AppConstants.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource,
    private val db: MoviesDatabase,
    private val initialPage: Int = 1
) : RemoteMediator<Int, Movie>() {
    
    private val TAG:String="MyTag"
    private val movieDao: MovieDao = db.getMovieDao()
    private val remoteKeyDao: RemoteKeyDao = db.getRemoteKeysDao()
    private var currentPage:Int? = null


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        return sample3(loadType = loadType, state)
    }

    private suspend fun sample4(loadType: LoadType,
                                state: PagingState<Int, Movie>):MediatorResult{

        var pageRequested: Int? = getPageForLastItem(state)
        var loadSize = STARTING_PAGE_INDEX * NUM_RESULTS_PER_PAGE

        if (loadType == LoadType.APPEND) {
            pageRequested = pageRequested?.plus(1)
                ?: return MediatorResult.Success(endOfPaginationReached = pageRequested != null)
        } else if (loadType == LoadType.REFRESH && pageRequested == null) {
            val cachedMovieCount = movieLocalDataSource.countMovies()
            loadSize = NUM_PAGES_IN_CACHE * NUM_RESULTS_PER_PAGE
            pageRequested = if (cachedMovieCount < loadSize) {
                1
            } else {
                // don't want to trigger API call
                null
            }
        } else {
            // don't want to trigger API call
            pageRequested = null
        }

        try {
            var endOfPaginationReached = false
            if (pageRequested != null) {
                // get more movies from api service
                val movieList: MutableList<Movie> = mutableListOf()
                while (loadSize > 0) {
                    val moviePage = movieRemoteDataSource.getPagedPopularMovies(pageRequested)
                    val movieListData:List<MovieDto> = moviePage.body()!!.results
                    val map:List<Movie> = movieListData.map {
                        it.toDomain(moviePage.body()!!.page)
                    }
                    movieDao.deleteAllMovies()
                    movieList.addAll(map)

                    movieList.map {
                            movie ->
                        movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
                    }
                    /*val currentDownloadPage=moviePage.body()!!.page
                    movieList.addAll(moviePage.body()!!.results)
                    movieList.map {movie->
                            movie.page=currentDownloadPage
                    }
                    movieList.map {
                            movie ->
                        movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
                    }*/

                    pageRequested += 1
                    loadSize -= NUM_RESULTS_PER_PAGE



                    db.withTransaction {
                        if(loadType==LoadType.REFRESH)
                        {
                            movieDao.deleteAllMovies()
                        }
                        movieDao.savePopularMovies(movieList)
                    }
                }

                /*if (dataRefreshManagerImpl.checkIfRefreshNeeded()) {
                    // delete and add
                    movieLocalDataSource.refreshMovies(movieList)
                } else {
                    // only add
                    movieLocalDataSource.addFreshPopularMovies(movieList)
                }*/
                endOfPaginationReached = movieList.isEmpty()
            }
            if (pageRequested == ENDING_PAGE_INDEX) {
                endOfPaginationReached = true
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun sample3(
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
            Log.d(TAG, "sample3: pageRequestNum $pageRequestNum")
            val response = movieRemoteDataSource.getPagedPopularMovies(pageRequestNum!!)
            currentPage= response.body()!!.page
            val moviesList:List<Movie> = convertMovieList(response.body()!!)
            Log.d(TAG, "sample3: Response string ${response.toString()}")

            val endOfPagination = moviesList.isEmpty()

            Log.d("MyTag ", "sample3: endOfPagination is $endOfPagination")

            db.withTransaction {
                    if(loadType==LoadType.REFRESH){
                        remoteKeyDao.deleteAllKeys()
                        movieDao.deleteAllMovies()
                    }

                val prevKey = if (pageRequestNum == STARTING_PAGE_INDEX) null else pageRequestNum - 1
                val nextKey = if (endOfPagination) null else pageRequestNum.plus(1)
                val keys = moviesList.map { movie ->
                    Log.d("MyTag", "load:  RemoteKeys $prevKey   $nextKey  ${movie.page}")
                    RemoteKey(movieId = movie.page, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAll(keys)
                moviesList.map { movie ->
                    movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
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

    private  fun convertMovieList(movieListRes:MovieList):List<Movie>{
        if(movieListRes.results.isNotEmpty())
        {
            val movies:List<Movie> = movieListRes.results.map {
                it.toDomain(movieListRes.page)
            }.sortedBy { it.page }
            return movies
        }
        return emptyList()
    }
    private suspend fun printRemoteKeys(remoteKey: RemoteKey?){
        if(remoteKey?.nextKey != null)
            Log.d(TAG, "sample3: Refresh ${remoteKey.nextKey}")
        else
            Log.d(TAG, "sample3: Refresh ${remoteKey?.toString()}")

    }
    private suspend fun getClosestKey(state: PagingState<Int, Movie>): RemoteKey? {

        return state.anchorPosition?.let { position ->
            Log.d("MyTag", "getClosestKey: position $position")
            state.closestItemToPosition(position)?.page.let {
                Log.d("MyTag", "getClosestKey: ${it}")
                remoteKeyDao.remoteKeyByMovieId(it!!)
            }
        }
    }

    private suspend fun getLastKey(state: PagingState<Int, Movie>): RemoteKey? {
        return state.lastItemOrNull().let {
            Log.d("MyTag", "getLastKey:  ${it!!.id}")
            remoteKeyDao.remoteKeyByMovieId(it.id)
        }
    }

    /*private suspend fun sample1(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.id
                }
            }

            val response = movieRemoteDataSource.getPagedPopularMovies(loadKey!!)
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getMovieDao().deleteAllMovies()
                    db.getRemoteKeysDao().deleteAllKeys()
                }
                val isEndOfList = response.body()!!.results.isEmpty()
                val prevKey = if (loadKey == STARTING_PAGE_INDEX) null else loadKey - 1
                val nextKey = if (isEndOfList) null else loadKey.plus(1)

                val keys = response.body()!!.results.map { movie ->
                    Log.d("MyTag", "load:  RemoteKeys $prevKey   $nextKey")
                    RemoteKey(movieId = movie.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.getRemoteKeysDao().insertAll(keys)
                response.body()!!.results.map { movie ->
                    movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
                }
                db.getMovieDao().savePopularMovies(response.body()!!.results)

            }
            MediatorResult.Success(
                endOfPaginationReached = response.body()!!.totalResults == loadKey
            )

        } catch (e: Exception) {
            Log.d("MyTag", "load: $e")
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.d("MyTag", "load: $e")
            return MediatorResult.Error(e)

        }
    }

    private suspend fun sample2(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        val pagedKeyData = getKeyPageData(loadType = loadType, state)

        val page = when (pagedKeyData) {
            is MediatorResult.Success -> {
                return pagedKeyData
            }
            else -> {
                pagedKeyData as Int
            }

        }
        try {

            val response = movieRemoteDataSource.getPagedPopularMovies(page = page)
            val isEndOfList = response.body()!!.results.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d("MyTag", "load: Refresh called page number is $page")
                    db.getMovieDao().deleteAllMovies()
                    db.getRemoteKeysDao().deleteAllKeys()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page.plus(1)
                Log.d("MyTag", "sample2:  PrevKey $prevKey ")
                val keys = response.body()!!.results.map { movie ->
                    Log.d("MyTag", "load:  RemoteKeys $prevKey   $nextKey")
                    RemoteKey(movieId = movie.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.getRemoteKeysDao().insertAll(keys)


                response.body()!!.results.map { movie ->
                    movie.posterPath = "https://image.tmdb.org/t/p/w500" + movie.posterPath
                }

                db.getMovieDao().savePopularMovies(response.body()!!.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (E: Exception) {
            return MediatorResult.Error(E)
        } catch (httpException: HttpException) {
            return MediatorResult.Error(httpException)
        }
    }*/

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Movie>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state = state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getFirstRemoteKey(state)
                val prevKey = remoteKey?.prevKey ?: MediatorResult.Success(false)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                val nextKey = remoteKey?.nextKey
                return nextKey ?: MediatorResult.Success(false)
            }
        }
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

    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie -> db.getRemoteKeysDao().remoteKeyByMovieId(movieId = movie.page) }
    }

    private fun getPageForLastItem(state: PagingState<Int, Movie>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.page
    }

    suspend fun reload() {
        val emptyPagingState = PagingState<Int, Movie>(
            emptyList(), null, PagingConfig(
                NUM_RESULTS_PER_PAGE
            ), 0
        )
        load(LoadType.REFRESH, emptyPagingState)
    }
    /**
     * Get the page of the last Movie item loaded from the database
     * Returns null if no data passed to Mediator
     */


}