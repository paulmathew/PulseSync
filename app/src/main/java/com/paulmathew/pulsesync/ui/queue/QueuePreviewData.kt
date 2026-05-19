package com.paulmathew.pulsesync.ui.queue

import com.paulmathew.pulsesync.model.QueuedOperation
import com.paulmathew.pulsesync.model.QueuedOperationMethod
import com.paulmathew.pulsesync.model.QueuedOperationStatus

object QueuePreviewData {

    val defaultState = QueueUiState(
        selectedFilter = QueueFilter.All,
        filters = listOf(
            QueueFilter.All,
            QueueFilter.Pending,
            QueueFilter.Syncing,
            QueueFilter.Failed
        ),
        operations = listOf(
            QueuedOperation(
                operationId = "op-1042",
                method = QueuedOperationMethod.Post,
                resourcePath = "/notes/42",
                enqueuedAt = "12:00:01",
                status = QueuedOperationStatus.Syncing,
                attemptCount = 1
            ),
            QueuedOperation(
                operationId = "op-1041",
                method = QueuedOperationMethod.Put,
                resourcePath = "/notes/41",
                enqueuedAt = "11:59:58",
                status = QueuedOperationStatus.Pending,
                attemptCount = 0
            ),
            QueuedOperation(
                operationId = "op-1040",
                method = QueuedOperationMethod.Delete,
                resourcePath = "/notes/40",
                enqueuedAt = "11:59:42",
                status = QueuedOperationStatus.Pending,
                attemptCount = 0
            ),
            QueuedOperation(
                operationId = "op-1039",
                method = QueuedOperationMethod.Post,
                resourcePath = "/attachments/88",
                enqueuedAt = "11:59:21",
                status = QueuedOperationStatus.Failed,
                attemptCount = 3,
                nextRetryLabel = "Retry in 18s"
            ),
            QueuedOperation(
                operationId = "op-1038",
                method = QueuedOperationMethod.Put,
                resourcePath = "/notes/39",
                enqueuedAt = "11:59:10",
                status = QueuedOperationStatus.Pending,
                attemptCount = 0
            )
        ),
        selectedOperation = null

    )
    val failedOperationSelectedState = defaultState.copy(
        selectedOperation = defaultState.operations.first {
            it.status == QueuedOperationStatus.Failed
        }
    )
}