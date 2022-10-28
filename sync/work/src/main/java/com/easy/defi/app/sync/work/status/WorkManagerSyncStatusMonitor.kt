package com.easy.defi.app.sync.work.status

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import com.easy.defi.app.core.data.util.SyncStatusMonitor
import com.easy.defi.app.sync.work.initializers.SyncWorkName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate

/**
 * [SyncStatusMonitor] backed by [WorkInfo] from [WorkManager]
 */
class WorkManagerSyncStatusMonitor @Inject constructor(
  @ApplicationContext context: Context,
) : SyncStatusMonitor {
  override val isSyncing: Flow<Boolean> =
    Transformations.map(
      WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName),
      MutableList<WorkInfo>::anyRunning,
    )
      .asFlow()
      .conflate()
}

private val List<WorkInfo>.anyRunning get() = any { it.state == State.RUNNING }
