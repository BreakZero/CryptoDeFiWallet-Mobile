package com.easy.defi.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.easy.defi.app.core.database.model.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
  @Query("SELECT * FROM TB_ASSET")
  fun assetsFlow(): Flow<List<AssetEntity>>

  @Query("SELECT * FROM TB_ASSET")
  suspend fun assets(): List<AssetEntity>

  @Query("select * from tb_asset where slug = :slug")
  suspend fun assetBySlug(slug: String): AssetEntity?

  @Query("select * from tb_asset where code = :code")
  suspend fun assetByChain(code: String): AssetEntity?

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAll(chains: List<AssetEntity>)

  @Query("DELETE FROM TB_ASSET")
  suspend fun deleteAll()

  @Query("SELECT * FROM TB_ASSET WHERE CONTRACT_ADDRESS = :contract")
  suspend fun findByContractAddress(contract: String): AssetEntity?

  @Update
  suspend fun update(entity: AssetEntity)

  @Transaction
  suspend fun updateBalanceViaSlug(slug: String, balance: String) {
    assetBySlug(slug)?.also {
      if (it.balance != balance) {
        update(it.copy(balance = balance))
      }
    }
  }

  @Transaction
  suspend fun updateBalanceForMainChain(chain: String, balance: String) {
    assetByChain(chain)?.also {
      if (it.balance != balance) {
        update(it.copy(balance = balance))
      }
    }
  }

  @Transaction
  suspend fun updateBalance(contract: String, balance: String) {
    findByContractAddress(contract)?.also {
      // only do update when the balance has changed
      if (it.balance != balance) {
        update(it.copy(balance = balance))
      }
    }
  }
}