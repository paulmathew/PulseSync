package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationMethod
import com.paulmathew.pulsesync.sync.SyncOperationStatus

object SyncRuntimePreviewScenario {

    fun operations(): List<SyncOperation> {
        return listOf(
            SyncOperation(
                id = "op-1042",
                method = SyncOperationMethod.Create,
                resourcePath = "/notes/42",
                payloadHash = "hash-notes-42",
                createdAtMillis = 0,
                attemptCount = 0,
                status = SyncOperationStatus.Pending
            ),
            SyncOperation(
                id = "op-1041",
                method = SyncOperationMethod.Update,
                resourcePath = "/notes/41",
                payloadHash = "hash-notes-41",
                createdAtMillis = 0,
                attemptCount = 1,
                status = SyncOperationStatus.Failed(
                    reason = com.paulmathew.pulsesync.sync.SyncFailureReason.Timeout,
                    nextRetryAtMillis = 15_000
                )
            )
        )
    }
}