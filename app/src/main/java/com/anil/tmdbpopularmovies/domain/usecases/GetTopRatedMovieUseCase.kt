package com.anil.tmdbpopularmovies.domain.usecases

import androidx.paging.PagingData
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopRatedMovieUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend fun getTopRatedMoviesList():Flow<PagingData<TopRatedMovieDto>>{
        return  moviesRepository.getTopRatedMovies()
    }
}