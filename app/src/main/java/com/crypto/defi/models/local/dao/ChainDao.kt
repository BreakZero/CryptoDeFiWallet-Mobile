package com.crypto.defi.models.local.dao

import androidx.room.*
import com.crypto.defi.models.local.entities.ChainEntity

@Dao
interface ChainDao {
    @Query("SELECT * FROM TB_CHAIN")
    suspend fun chains(): List<ChainEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chains: List<ChainEntity>)

    @Query("DELETE FROM TB_CHAIN")
    suspend fun deleteAll()
}