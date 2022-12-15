package com.anil.tmdbpopularmovies.domain.usecases

import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import javax.inject.Inject

open class BaseUseCase @Inject constructor(val repository: MoviesRepository) {
    var getMoviesUseCase:GetMoviesUseCase?=null
    var getTopRatedMovieUseCase:GetTopRatedMovieUseCase?=null
    var getSearchMovieUse:GetSearchMovieUse?=null
    init {
        getMoviesUseCase=GetMoviesUseCase(repository)
         getTopRatedMovieUseCase= GetTopRatedMovieUseCase(repository)
        getSearchMovieUse=GetSearchMovieUse(repository)
    }
}