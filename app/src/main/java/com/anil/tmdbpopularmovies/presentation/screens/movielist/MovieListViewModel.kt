package com.anil.tmdbpopularmovies.presentation.screens.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anil.tmdbpopularmovies.data.local.dto.MovieDto
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto

import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.domain.usecases.BaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private val baseUseCase: BaseUseCase) :
    ViewModel() {

    sealed class MovieListUIState{
        data class Success(val data:Any):MovieListUIState()
        object Loading:MovieListUIState()
        data class Error(val errorMsg:String):MovieListUIState()
    }
    var movieList: Flow<PagingData<Movie>>
     var topRatedMoviesList:Flow<PagingData<TopRatedMovieDto>>
     var searchMovieList:Flow<PagingData<MovieDto>>
    init {
        movieList = loadPagedMovies()
        topRatedMoviesList=loadTopRatedMovies()
        searchMovieList=loadSearchMovieData("aven")
    }

    private fun loadSearchMovieData(searchText: String): Flow<PagingData<MovieDto>> {
        var searchMovieList:Flow<PagingData<MovieDto>> = emptyFlow()
        viewModelScope.launch(block = {
            searchMovieList =
                baseUseCase.getSearchMovieUse!!.getSearchMovieList(searchText = searchText)
                    .cachedIn(viewModelScope)
        })
        return searchMovieList
    }

    private fun loadTopRatedMovies(): Flow<PagingData<TopRatedMovieDto>> {

        var movieListRepo:Flow<PagingData<TopRatedMovieDto>> = emptyFlow()
        viewModelScope.launch {
                movieListRepo=    baseUseCase.getTopRatedMovieUseCase!!.getTopRatedMoviesList()
        }
        return movieListRepo
    }

    private fun loadPagedMovies(): Flow<PagingData<Movie>> {

        var movieListRepo: Flow<PagingData<Movie>> = emptyFlow()
        viewModelScope.launch {
            movieListRepo = baseUseCase.getMoviesUseCase!!.getPagedPopularMovies().cachedIn(viewModelScope)
        }
        return movieListRepo

    }

}
