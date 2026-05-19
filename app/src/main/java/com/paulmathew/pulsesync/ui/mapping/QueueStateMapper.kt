package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.model.QueuedOperation
import com.paulmathew.pulsesync.model.QueuedOperationMethod
import com.paulmathew.pulsesync.model.QueuedOperationStatus
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationMethod
import com.paulmathew.pulsesync.sync.SyncOperationStatus
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.queue.QueueFilter
import com.paulmathew.pulsesync.ui.queue.QueueUiState

fun SyncRuntimeState.toQueueUiState(
    selectedFilter: QueueFilter = QueueFilter.All,
    selectedOperationId: String? = null
): QueueUiState {
    val queuedOperations = operations
        .mapNotNull { it.toQueuedOperationOrNull() }
        .filter { it.matches(selectedFilter) }
    return QueueUiState(
        selectedFilter = selectedFilter,
        filters = listOf(
            QueueFilter.All,
            QueueFilter.Pending,
            QueueFilter.Syncing,
            QueueFilter.Failed
        ),
        selectedOperation = queuedOperations.firstOrNull {
            it.operationId == selectedOperationId
        },
        operations = queuedOperations
    )
}

private fun SyncOperation.toQueuedOperation(): QueuedOperation {
    return QueuedOperation(
        operationId = id,
        method = method.toQueuedOperationMethod(),
        resourcePath = resourcePath,
        enqueuedAt = createdAtMillis.toTimestampLabel(),
        status = requireNotNull(status.toQueuedStatusOrNull()),
        attemptCount = attemptCount,
        nextRetryLabel = status.nextRetryLabel()
    )
}

private fun SyncOperationMethod.toQueuedOperationMethod(): QueuedOperationMethod {
    return when (this) {
        SyncOperationMethod.Create -> QueuedOperationMethod.Post
        SyncOperationMethod.Update -> QueuedOperationMethod.Put
        SyncOperationMethod.Delete -> QueuedOperationMethod.Delete
    }
}

private fun SyncOperationStatus.toQueuedStatusOrNull(): QueuedOperationStatus? {
    return when (this) {
        SyncOperationStatus.Pending -> QueuedOperationStatus.Pending
        SyncOperationStatus.InFlight -> QueuedOperationStatus.Syncing
        is SyncOperationStatus.Failed -> QueuedOperationStatus.Failed
        SyncOperationStatus.Synced,
        SyncOperationStatus.Cancelled -> null
    }
}

private fun SyncOperationStatus.nextRetryLabel(): String? {
    return when (this) {
        is SyncOperationStatus.Failed -> {
            nextRetryAtMillis?.let { "Retry at ${it.toTimestampLabel()}" }
        }
        else -> null
    }
}

private fun Long.toTimestampLabel(): String {
    val totalSeconds = this / 1_000
    val minutes = (totalSeconds / 60) % 60
    val seconds = totalSeconds % 60

    return "%02d:%02d".format(minutes, seconds)
}
private fun SyncOperation.toQueuedOperationOrNull(): QueuedOperation? {
    val queuedStatus = status.toQueuedStatusOrNull() ?: return null

    return QueuedOperation(
        operationId = id,
        method = method.toQueuedOperationMethod(),
        resourcePath = resourcePath,
        enqueuedAt = createdAtMillis.toRuntimeTimestampLabel(),
        status = queuedStatus,
        attemptCount = attemptCount,
        nextRetryLabel = status.nextRetryLabel()
    )
}

private fun QueuedOperation.matches(filter: QueueFilter): Boolean {
    return when (filter) {
        QueueFilter.All -> true
        QueueFilter.Pending -> status == QueuedOperationStatus.Pending
        QueueFilter.Syncing -> status == QueuedOperationStatus.Syncing
        QueueFilter.Failed -> status == QueuedOperationStatus.Failed
    }
}