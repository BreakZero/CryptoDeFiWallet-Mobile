package com.crypto.defi

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crypto.defi.models.local.CryptoDeFiDatabase
import com.crypto.defi.models.local.dao.CoinVersionDao
import com.crypto.defi.models.local.entities.CoinVersionShaEntity
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

  private lateinit var versionDao: CoinVersionDao
  private lateinit var db: CryptoDeFiDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, CryptoDeFiDatabase::class.java)
      .build()
    versionDao = db.versionDao
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    db.close()
  }

  @Test
  @Throws(Exception::class)
  fun writeWalletAndReadInList() = runTest {
    val versionEntity = CoinVersionShaEntity(
      sha256 = "mocksha256",
      createAt = 10000L,
    )

    versionDao.insert(versionEntity)
    val lastVersion = versionDao.lastVersion()

    assertEquals(lastVersion?.sha256, "mocksha256")
  }
}
