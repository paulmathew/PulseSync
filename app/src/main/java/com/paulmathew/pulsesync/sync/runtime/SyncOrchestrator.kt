package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.model.NetworkProfile
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.conflict.ConflictResolutionResult
import com.paulmathew.pulsesync.sync.conflict.ConflictResolutionStrategy
import kotlinx.coroutines.flow.StateFlow

interface SyncOrchestrator {
    val state: StateFlow<SyncRuntimeState>

    fun enqueue(operation: SyncOperation)

    fun startNext(nowMillis: Long)

    fun completeActive(
        result: SyncAttemptResult,
        nowMillis: Long
    )
    fun resolveConflict(
        operationId: String,
        strategy: ConflictResolutionStrategy,
        resolvedAtMillis: Long
    ): ConflictResolutionResult?

    fun selectNetworkProfile(profile: NetworkProfile)

    fun setNetworkSimulationRunning(isRunning: Boolean)

    fun completeActiveUsingNetworkProfile(nowMillis: Long)
}