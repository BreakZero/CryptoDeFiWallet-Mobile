package com.crypto.defi.models.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crypto.defi.models.local.entities.CoinVersionShaEntity

@Dao
interface CoinVersionDao {
  @Query("SELECT * FROM TB_VERSION_SHA256 ORDER BY create_at DESC LIMIT 1")
  suspend fun lastVersion(): CoinVersionShaEntity?

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(shaEntity: CoinVersionShaEntity)

  @Query("DELETE FROM TB_VERSION_SHA256")
  suspend fun deleteAll()
}
