package com.anil.tmdbpopularmovies.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anil.tmdbclientapp.data.model.movie.MovieList
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val remoteDataSource: MovieRemoteDataSource,
    private val searchKey:String
):PagingSource<Int,MovieDto>() {


    override suspend fun load(params: PagingSource.LoadParams<Int>): LoadResult<Int, MovieDto> {

        try {
            val position = params.key ?: 1
            val response=remoteDataSource.getSearchMovieByTitle(searchKey)
            var resultList:List<MovieDto> = emptyList()
            if(response.isSuccessful && response.code()==200){
                response.body()!!.let {
                    resultList=it.searchMovieResults
                }
            }
            return LoadResult.Page(
                data = resultList,
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (ioException: IOException) {
            Log.e("MyTag", "load: Error ${ioException.message}")
            return PagingSource.LoadResult.Error(ioException)
        } catch (httpException: HttpException) {
            Log.e("MyTag", "load: Error ${httpException.message}")
            return PagingSource.LoadResult.Error(httpException)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }
}