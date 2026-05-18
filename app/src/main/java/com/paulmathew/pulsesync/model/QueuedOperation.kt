package com.paulmathew.pulsesync.model

data class QueuedOperation(
    val operationId: String,
    val method: QueuedOperationMethod,
    val resourcePath: String,
    val enqueuedAt: String,
    val status: QueuedOperationStatus,
    val attemptCount: Int,
    val nextRetryLabel: String? = null
)

enum class QueuedOperationMethod {
    Post,
    Put,
    Delete
}

enum class QueuedOperationStatus {
    Pending,
    Syncing,
    Failed
}