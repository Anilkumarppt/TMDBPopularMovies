package com.anil.tmdbpopularmovies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedRemoteKey

@Dao
interface TopMovieRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTopMovieRemoteKeys(remoteKey: List<TopRatedRemoteKey>)

    @Query("SELECT * FROM TOP_RATED_REMOTE_KEY WHERE movieId=:movieId")
    suspend fun remoteKeyByTopMovieId(movieId: Int): TopRatedRemoteKey?

    @Query("DELETE FROM TOP_RATED_REMOTE_KEY")
    suspend fun deleteAllTopMovieKeys()
}