package com.anil.tmdbpopularmovies.domain.usecases

import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class MovieDetailUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

     suspend fun getMovieItemById(movieId: Int): Flow<Movie?> {
        return moviesRepository.getMovieById(movieId = movieId)
        //return emptyFlow()
    }


}