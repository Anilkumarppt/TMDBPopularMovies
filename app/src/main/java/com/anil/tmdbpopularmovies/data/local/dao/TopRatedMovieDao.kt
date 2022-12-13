package com.anil.tmdbpopularmovies.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedMovieDto
import com.anil.tmdbpopularmovies.data.remote.model.Movie
import com.anil.tmdbpopularmovies.data.remote.model.TopRatedMovie
import kotlinx.coroutines.flow.Flow


@Dao
interface TopRatedMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopRatedMovies(movies:List<TopRatedMovieDto>)

    @Query("SELECT * FROM top_rated_movie")
    suspend fun getTopRatedMoviesFromDB(): List<TopRatedMovieDto>

    @Query("SELECT * FROM top_rated_movie ORDER BY page ASC, voteAverage DESC ")
    fun getTopRatedMoviesPagingSource(): PagingSource<Int, TopRatedMovieDto>

    @Query("DELETE FROM top_rated_movie ")
    suspend fun deleteAllMovies()

    @Query("SELECT COUNT(id) FROM  top_rated_movie")
    suspend fun countMovies(): Int

    @Transaction
    @Query("SELECT * FROM top_rated_movie WHERE id= :movieId")
    fun getFullTopRatedMovieData(movieId:Int): Flow<TopRatedMovieDto?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopRatedMovie(movie: TopRatedMovieDto)

}