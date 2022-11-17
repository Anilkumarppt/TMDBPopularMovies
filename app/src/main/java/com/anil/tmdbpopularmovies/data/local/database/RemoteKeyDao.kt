package com.anil.tmdbpopularmovies.data.local.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM REMOTE_KEYS WHERE movieId=:movieId")
    suspend fun remoteKeyByMovieId(movieId: Int): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllKeys()
}