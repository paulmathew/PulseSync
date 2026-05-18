package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationMethod
import com.paulmathew.pulsesync.sync.SyncOperationStatus

object SyncRuntimeSeed {

    fun seed(orchestrator: SyncOrchestrator) {
        seedOperations().forEach(orchestrator::enqueue)
    }

    private fun seedOperations(): List<SyncOperation> {
        return listOf(
            SyncOperation(
                id = "op-1042",
                method = SyncOperationMethod.Create,
                resourcePath = "/notes/42",
                payloadHash = "hash-notes-42",
                createdAtMillis = 1_000,
                attemptCount = 0,
                status = SyncOperationStatus.Pending
            ),
            SyncOperation(
                id = "op-1041",
                method = SyncOperationMethod.Update,
                resourcePath = "/notes/41",
                payloadHash = "hash-notes-41",
                createdAtMillis = 2_000,
                attemptCount = 0,
                status = SyncOperationStatus.Pending
            ),
            SyncOperation(
                id = "op-1040",
                method = SyncOperationMethod.Delete,
                resourcePath = "/notes/40",
                payloadHash = "hash-notes-40",
                createdAtMillis = 3_000,
                attemptCount = 0,
                status = SyncOperationStatus.Pending
            )
        )
    }
}