package com.crypto.wallet

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crypto.wallet.model.WalletDao
import com.crypto.wallet.model.WalletDatabase
import com.crypto.wallet.model.WalletEntity
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class WalletEntityReadWriteTest {
    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var walletDao: WalletDao
    private lateinit var walletDb: WalletDatabase

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
    @Throws(Exception::class)
    fun writeWalletAndReadInList() = runTest {
        val walletEntity = WalletEntity(
            mnemonic = "mock mnemonic",
            active = 1,
            passphrase = ""
        )

        walletDao.insertWallet(walletEntity)
        val activeOne = walletDao.activeWallet()

        assertEquals(activeOne?.active, 1)
    }
}