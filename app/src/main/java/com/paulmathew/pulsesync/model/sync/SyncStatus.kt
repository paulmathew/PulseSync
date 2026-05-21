package com.paulmathew.pulsesync.model.sync

sealed interface SyncStatus {
    data object Synced : SyncStatus
    data object Syncing : SyncStatus
    data class OfflinePending(
        val pendingChanges: Int
    ) : SyncStatus

    data class Retrying(
        val attemptLabel: String
    ) : SyncStatus

    data object NeedsAttention : SyncStatus
}