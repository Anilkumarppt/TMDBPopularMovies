package com.anil.tmdbpopularmovies.presentation.screens.details

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import com.anil.tmdbpopularmovies.domain.usecases.MovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel  @Inject constructor(private val movieDetailUseCase: MovieDetailUseCase):ViewModel() {

    var movie: Flow<Movie?> = emptyFlow()
    private  val TAG = "MovieDetailsViewModel"

    init {
        movie=getMovieById(null)
    }
    /**
     * Gets the full data of a movie
     * Includes a trailer
     */
    fun getMovieById(movieId: Int?): Flow<Movie?> {
        if (movieId != null) {
            viewModelScope.launch {
                movie = movieDetailUseCase.getMovieItemById(movieId)
                    .distinctUntilChanged()
                    .map {
                        it
                    }
            }
        }
        return movie
    }

}