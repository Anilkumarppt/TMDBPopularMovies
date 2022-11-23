package com.anil.tmdbpopularmovies.data.local

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = REPLACE)
    suspend fun savePopularMovies(movies:List<Movie>)

    @Query("SELECT * FROM popular_movie")
    suspend fun getPopularMoviesFromDB(): List<Movie>

    @Query("SELECT * FROM popular_movie ORDER BY page ASC, voteAverage DESC ")
    fun getMoviesPagingSource(): PagingSource<Int, Movie>

    @Query("DELETE FROM POPULAR_MOVIE ")
    suspend fun deleteAllMovies()

    @Query("SELECT COUNT(id) FROM  popular_movie")
    suspend fun countMovies(): Int

    @Transaction
    @Query("SELECT * FROM popular_movie WHERE id= :movieId")
    fun getFullMovieData(movieId:Int): Flow<Movie?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)


}