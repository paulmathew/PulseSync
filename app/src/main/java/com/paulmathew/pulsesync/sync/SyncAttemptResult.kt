package com.paulmathew.pulsesync.sync

sealed interface SyncAttemptResult {
    data object Success : SyncAttemptResult

    data class Failure(
        val reason: SyncFailureReason
    ) : SyncAttemptResult
}