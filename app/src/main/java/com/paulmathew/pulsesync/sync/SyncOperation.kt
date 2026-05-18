package com.paulmathew.pulsesync.sync

data class SyncOperation(
    val id: String,
    val method: SyncOperationMethod,
    val resourcePath: String,
    val payloadHash: String,
    val createdAtMillis: Long,
    val attemptCount: Int,
    val status: SyncOperationStatus
)

enum class SyncOperationMethod {
    Create,
    Update,
    Delete
}

sealed interface SyncOperationStatus {
    data object Pending : SyncOperationStatus
    data object InFlight : SyncOperationStatus
    data class Failed(
        val reason: SyncFailureReason,
        val nextRetryAtMillis: Long?
    ) : SyncOperationStatus

    data object Synced : SyncOperationStatus
    data object Cancelled : SyncOperationStatus
}