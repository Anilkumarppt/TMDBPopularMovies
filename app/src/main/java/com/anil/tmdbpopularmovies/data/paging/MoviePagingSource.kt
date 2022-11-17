package com.anil.tmdbpopularmovies.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MoviePagingSource(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource
) {
/*
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        try {
            val position = params.key ?: 1
            val localResponse = localDataSource.getLocalPagedMovies()
            lateinit var remoteResponse: List<Movie>
            if (localResponse.isEmpty())
                remoteResponse = remoteDataSource.getPagedPopularMovies(position)
            if (!remoteResponse.isEmpty()) {
                localDataSource.addFreshPopularMovies(remoteResponse)
            }
            return LoadResult.Page(
                data = remoteResponse,
                prevKey = null,
                nextKey = if (remoteResponse.isNotEmpty()) remoteResponse.position + 1 else null
            )
        } catch (ioException: IOException) {
            Log.e("MyTag", "load: Error ${ioException.message}")
            return LoadResult.Error(ioException)
        } catch (httpException: HttpException) {
            Log.e("MyTag", "load: Error ${httpException.message}")
            return LoadResult.Error(httpException)
        }

    }*/
}