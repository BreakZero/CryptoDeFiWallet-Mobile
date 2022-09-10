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

    @Query("SELECT * FROM TB_ASSET WHERE SLUG = :slug")
    suspend fun findBySlug(slug: String): AssetEntity?

    @Update
    suspend fun update(entity: AssetEntity)

    @Transaction
    suspend fun updateBalance(slug: String, balance: String) {
        findBySlug(slug)?.also {
            // only do update when the balance has changed
            if (it.balance != balance) {
                update(it.copy(balance = balance))
            }
        }
    }
}