package com.paulmathew.pulsesync.sync.conflict

sealed interface ConflictResolutionStrategy {
    data object LocalWins : ConflictResolutionStrategy
    data object RemoteWins : ConflictResolutionStrategy
    data class Merge(
        val mergedSummary: String,
        val mergedPayloadHash: String
    ) : ConflictResolutionStrategy
    data object ManualReview : ConflictResolutionStrategy
}

sealed interface ConflictResolutionResult {
    data class Resolved(
        val operationId: String,
        val resourcePath: String,
        val resolvedVersion: ConflictingVersion,
        val strategy: ConflictResolutionStrategy
    ) : ConflictResolutionResult

    data class RequiresManualReview(
        val conflict: SyncConflict
    ) : ConflictResolutionResult
}