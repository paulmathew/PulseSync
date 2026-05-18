package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncOperation
import kotlinx.coroutines.flow.StateFlow

interface SyncOrchestrator {
    val state: StateFlow<SyncRuntimeState>

    fun enqueue(operation: SyncOperation)

    fun startNext(nowMillis: Long)

    fun completeActive(
        result: SyncAttemptResult,
        nowMillis: Long
    )
}