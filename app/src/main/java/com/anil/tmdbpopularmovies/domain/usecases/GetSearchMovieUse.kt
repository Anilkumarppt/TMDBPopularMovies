package com.anil.tmdbpopularmovies.domain.usecases

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchMovieUse @Inject constructor(private val moviesRepository: MoviesRepository)  {

    suspend fun getSearchMovieList( searchText:String):Flow<PagingData<MovieDto>>{
        return moviesRepository.getSearchMovieByTitle(searchText)
    }
}