package com.anil.tmdbpopularmovies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anil.tmdbpopularmovies.data.local.dto.CastDto

@Dao
interface CastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCastList(castList:List<CastDto>)

    @Query("Select * from movie_casts ORDER BY popularity ASC")
    suspend fun getCastList():List<CastDto>

    @Query("Delete from movie_casts")
    suspend fun deleteCastList()

}