package com.crypto.defi

import com.crypto.defi.chains.EmptyChain
import org.junit.Assert
import org.junit.Test

class ConstantUnitTest {
  @Test
  fun test_empty_chain() {
    val emptyChain = EmptyChain()
    Assert.assertEquals(emptyChain.address(), "")
  }
}