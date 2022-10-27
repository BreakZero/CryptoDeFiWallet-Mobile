package com.easy.defi.app.core.datastore

import androidx.datastore.core.DataStore
import com.easy.defi.app.core.model.data.ChainNetwork
import com.easy.defi.app.core.model.data.DeFiCurrency
import com.easy.defi.app.core.model.data.UserData
import com.easy.defi.app.core.model.data.WalletProfile
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import timber.log.Timber

class UserPreferencesDataSource @Inject constructor(
  private val userPreferences: DataStore<UserPreferences>,
) {
  val userDataStream = userPreferences.data.map {
    UserData(
      currency = DeFiCurrency(it.currencyCode, it.currencySymbol),
      network = ChainNetwork.valueOf(it.network),
      walletProfile = WalletProfile(avator = it.avator, walletName = it.walletName),
    )
  }

  suspend fun setAvator(avatorUrl: String) {
    try {
      userPreferences.updateData {
        it.copy {
          avator = avatorUrl
        }
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  suspend fun setWalletName(walletName: String) {
    try {
      userPreferences.updateData {
        it.copy {
          this.walletName = walletName
        }
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  suspend fun setCurrency(currency: DeFiCurrency) {
    try {
      userPreferences.updateData {
        it.copy {
          this.currencyCode = currency.code
          this.currencySymbol = currency.symbol
        }
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  suspend fun setNetwork(network: ChainNetwork) {
    try {
      userPreferences.updateData {
        it.copy {
          this.network = network.label
        }
      }
    } catch (e: Exception) {
      Timber.e(e)
    }
  }
}
