package com.anil.tmdbpopularmovies.domain.usecases

import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import javax.inject.Inject

data class BaseUseCase (var getMoviesUseCase:GetMoviesUseCase,
                        var getTopRatedMovieUseCase:GetTopRatedMovieUseCase,
                        var getSearchMovieUse:GetSearchMovieUse,
                        var getMovieDetailUseCase: MovieDetailUseCase) {
    /*init {
        getMoviesUseCase=GetMoviesUseCase(repository)
         getTopRatedMovieUseCase= GetTopRatedMovieUseCase(repository)
        getSearchMovieUse= GetSearchMovieUse(repository)
    }*/
}