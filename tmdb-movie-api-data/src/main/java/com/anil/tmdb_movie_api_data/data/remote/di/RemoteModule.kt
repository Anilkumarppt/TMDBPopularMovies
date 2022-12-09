package com.anil.tmdb_movie_api_data.data.remote.di

import com.anil.tmdb_movie_api_data.data.AppConstants.BASE_URL
import com.anil.tmdb_movie_api_data.data.AppConstants.CONNECT_TIMEOUT
import com.anil.tmdb_movie_api_data.data.AppConstants.READ_TIMEOUT
import com.anil.tmdb_movie_api_data.data.AppConstants.WRITE_TIMEOUT
import com.anil.tmdb_movie_api_data.data.remote.AuthenticationInterceptors
import com.anil.tmdb_movie_api_data.data.remote.MovieRemoteDataSource
import com.anil.tmdb_movie_api_data.data.remote.MovieRemoteDataSourceImpl
import com.anil.tmdb_movie_api_data.data.remote.apiservice.MoviesAPIService
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
object RemoteModule {
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
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
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


}