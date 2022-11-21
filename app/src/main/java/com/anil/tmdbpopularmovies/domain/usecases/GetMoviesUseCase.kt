package com.anil.tmdbpopularmovies.domain.usecases

import androidx.paging.PagingData

import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend fun getPagedPopularMovies(): Flow<PagingData<Movie>>{
        return moviesRepository.getMostPopularMovies()
    }

}