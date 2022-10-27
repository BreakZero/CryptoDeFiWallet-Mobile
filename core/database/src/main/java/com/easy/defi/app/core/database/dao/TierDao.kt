package com.easy.defi.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.easy.defi.app.core.database.model.TierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TierDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(tier: List<TierEntity>)

  @Query("SELECT * FROM tb_tier WHERE to_currency = :toCurrency")
  fun allTiers(toCurrency: String): Flow<List<TierEntity>>

  @Query("SELECT * FROM TB_TIER WHERE from_slug = :slug")
  fun findBySlug(slug: String): Flow<TierEntity?>
}
