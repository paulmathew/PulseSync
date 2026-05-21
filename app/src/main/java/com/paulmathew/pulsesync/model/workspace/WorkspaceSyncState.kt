package com.paulmathew.pulsesync.model.workspace

sealed interface WorkspaceSyncState {
    data object Synced : WorkspaceSyncState

    data class Syncing(
        val pendingChanges: Int
    ) : WorkspaceSyncState

    data class Offline(
        val pendingChanges: Int
    ) : WorkspaceSyncState

    data class NeedsAttention(
        val reason: String
    ) : WorkspaceSyncState
}