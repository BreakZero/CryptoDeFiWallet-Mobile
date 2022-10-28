package com.easy.defi.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.easy.defi.app.core.database.model.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWallet(entity: WalletEntity)

  @Delete
  suspend fun deleteWallet(entity: WalletEntity)

  @Query(
    """
    SELECT *
    FROM TB_WALLET
    WHERE active = 1
    """,
  )
  fun activeWallet(): Flow<WalletEntity?>
}
