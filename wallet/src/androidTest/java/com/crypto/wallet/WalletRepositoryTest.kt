package com.crypto.wallet

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crypto.wallet.model.Wallet
import com.crypto.wallet.model.WalletDao
import com.crypto.wallet.model.WalletDatabase
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class WalletRepositoryTest {

  @get:Rule
  val dispatcherRule = TestDispatcherRule()

  private lateinit var walletDao: WalletDao
  private lateinit var walletDb: WalletDatabase

  private val wallet = Wallet(
    mnemonic = "mock mnemonic",
    active = 1,
    passphrase = "",
  )

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    walletDb = Room.inMemoryDatabaseBuilder(context, WalletDatabase::class.java)
      .build()
    walletDao = walletDb.walletDao
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    walletDb.close()
  }

  @Test
  fun walletRepository_test() = runTest {
    val walletRepository = WalletRepository { walletDb }
    walletRepository.insertWallet(wallet)

    val activeOne = walletRepository.activeOne()
    Assert.assertEquals(activeOne, wallet)

    walletRepository.deleteOne(wallet)
    val activeOne1 = walletRepository.activeOne()

    Assert.assertNull(activeOne1)
  }
}
