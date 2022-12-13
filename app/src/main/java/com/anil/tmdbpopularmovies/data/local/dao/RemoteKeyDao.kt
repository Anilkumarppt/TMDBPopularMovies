package com.anil.tmdbpopularmovies.data.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.anil.tmdbpopularmovies.data.local.dto.RemoteKey
import com.anil.tmdbpopularmovies.data.local.dto.TopRatedRemoteKey

@Dao
interface RemoteKeyDao {

    //Popular movie remote key operations

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM REMOTE_KEYS WHERE movieId=:movieId")
    suspend fun remoteKeyByMovieId(movieId: Int): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllKeys()

    //TopRated Movie Remote Key Operations



}