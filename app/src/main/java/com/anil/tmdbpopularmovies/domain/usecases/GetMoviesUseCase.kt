package com.anil.tmdbpopularmovies.domain.usecases

import androidx.paging.PagingData
import com.anil.tmdbpopularmovies.data.MovieDataSource
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend fun getPagedPopularMovies(): Flow<PagingData<Movie>>{
        return moviesRepository.getMostPopularMovies()
    }

}