package com.easy.defi.app.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Reports on if synchronization is in progress
 */
interface SyncStatusMonitor {
  val isSyncing: Flow<Boolean>
}
