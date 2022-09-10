package com.crypto.defi.models.local.dao

import androidx.room.*
import com.crypto.defi.models.local.entities.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM TB_ASSET")
    fun assetsFlow(): Flow<List<AssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chains: List<AssetEntity>)

    @Query("DELETE FROM TB_ASSET")
    suspend fun deleteAll()

    @Transaction
    suspend fun s() {
        deleteAll()
    }
}