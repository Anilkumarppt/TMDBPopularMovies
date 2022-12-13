package com.anil.tmdbpopularmovies.presentation.screens.movielist

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto

import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import com.anil.tmdbpopularmovies.data.repository.MoviesRepositoryImpl
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import com.anil.tmdbpopularmovies.domain.usecases.GetMoviesUseCase
import com.anil.tmdbpopularmovies.domain.usecases.GetTopRatedMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val getMoviesUseCase: GetMoviesUseCase,private val getTopRatedMovieUseCase: GetTopRatedMovieUseCase) :
    ViewModel() {

    sealed class MovieListUIState{
        data class Success(val data:Any):MovieListUIState()
        object Loading:MovieListUIState()
        data class Error(val errorMsg:String):MovieListUIState()
    }
    var movieList: Flow<PagingData<Movie>>
     var topRatedMoviesList:Flow<PagingData<TopRatedMovieDto>>
    init {
        movieList = loadPagedMovies()
        topRatedMoviesList=loadTopRatedMovies()
    }

    private fun loadTopRatedMovies(): Flow<PagingData<TopRatedMovieDto>> {

        var movieListRepo:Flow<PagingData<TopRatedMovieDto>> = emptyFlow()
        viewModelScope.launch {
                movieListRepo=    getTopRatedMovieUseCase.getTopRatedMoviesList()
        }
        return movieListRepo
    }

    private fun loadPagedMovies(): Flow<PagingData<Movie>> {

        var movieListRepo: Flow<PagingData<Movie>> = emptyFlow()
        viewModelScope.launch {
            movieListRepo = getMoviesUseCase.getPagedPopularMovies().cachedIn(viewModelScope)
        }
        return movieListRepo

    }

}
