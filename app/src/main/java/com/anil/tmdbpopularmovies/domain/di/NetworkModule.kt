package com.anil.tmdbpopularmovies.domain.di

import com.anil.tmdbpopularmovies.data.local.dao.MovieDao
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSource
import com.anil.tmdbpopularmovies.data.local.MovieLocalDataSourceImpl
import com.anil.tmdbpopularmovies.data.local.dao.TopRatedMovieDao
import com.anil.tmdbpopularmovies.data.local.database.MoviesDatabase
import com.anil.tmdbpopularmovies.data.paging.TopRatedMovieRemoteMeditor
import com.anil.tmdbpopularmovies.data.remote.apiservice.MoviesAPIService
import com.anil.tmdbpopularmovies.data.remote.AuthenticationInterceptors
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSource
import com.anil.tmdbpopularmovies.data.remote.MovieRemoteDataSourceImpl
import com.anil.tmdbpopularmovies.data.repository.MoviesRepositoryImpl
import com.anil.tmdbpopularmovies.domain.repository.MoviesRepository
import com.anil.tmdbpopularmovies.domain.usecases.GetMoviesUseCase
import com.anil.tmdbpopularmovies.domain.usecases.GetTopRatedMovieUseCase
import com.anil.tmdbpopularmovies.domain.usecases.MovieDetailUseCase
import com.anil.tmdbpopularmovies.util.AppConstants.BASE_URL
import com.anil.tmdbpopularmovies.util.AppConstants.CONNECT_TIMEOUT
import com.anil.tmdbpopularmovies.util.AppConstants.READ_TIMEOUT
import com.anil.tmdbpopularmovies.util.AppConstants.WRITE_TIMEOUT
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit.Builder {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authenticationInterceptors: AuthenticationInterceptors): OkHttpClient {

        val loggingInterceptor: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient().newBuilder().addInterceptor(loggingInterceptor)
            .addInterceptor(authenticationInterceptors)
            .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)
            .build()

    }

    @Provides
    @Singleton
    fun provideMovieAPI(retrofit: Retrofit.Builder): MoviesAPIService {
        return retrofit.build().create(MoviesAPIService::class.java)
    }


    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(moviesAPIService: MoviesAPIService): MovieRemoteDataSource =
        MovieRemoteDataSourceImpl(moviesAPIService = moviesAPIService)





    /* @Provides
     @Singleton
     fun provideLocalDataSource(movieDao:MovieDao):MovieLocalDataSource=MovieLocalDataSourceImpl(movieDao = movieDao)*/

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
        movieLocalDataSource: MovieLocalDataSource,
        movieDataBase: MoviesDatabase
    ): MoviesRepository = MoviesRepositoryImpl(
        movieRemoteDataSource = movieRemoteDataSource,
        movieLocalDataSource = movieLocalDataSource,
        db = movieDataBase
    )




}