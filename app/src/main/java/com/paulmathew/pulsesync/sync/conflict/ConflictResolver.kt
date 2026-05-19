package com.paulmathew.pulsesync.sync.conflict

object ConflictResolver {

    fun resolve(
        conflict: SyncConflict,
        strategy: ConflictResolutionStrategy,
        resolvedAtMillis: Long
    ): ConflictResolutionResult {
        return when (strategy) {
            ConflictResolutionStrategy.LocalWins -> ConflictResolutionResult.Resolved(
                operationId = conflict.operationId,
                resourcePath = conflict.resourcePath,
                resolvedVersion = conflict.localVersion.copy(
                    updatedAtMillis = resolvedAtMillis
                ),
                strategy = strategy
            )

            ConflictResolutionStrategy.RemoteWins -> ConflictResolutionResult.Resolved(
                operationId = conflict.operationId,
                resourcePath = conflict.resourcePath,
                resolvedVersion = conflict.remoteVersion.copy(
                    updatedAtMillis = resolvedAtMillis
                ),
                strategy = strategy
            )

            is ConflictResolutionStrategy.Merge -> ConflictResolutionResult.Resolved(
                operationId = conflict.operationId,
                resourcePath = conflict.resourcePath,
                resolvedVersion = ConflictingVersion(
                    versionId = "merged-${conflict.operationId}",
                    updatedAtMillis = resolvedAtMillis,
                    payloadHash = strategy.mergedPayloadHash,
                    summary = strategy.mergedSummary
                ),
                strategy = strategy
            )

            ConflictResolutionStrategy.ManualReview -> ConflictResolutionResult.RequiresManualReview(
                conflict = conflict
            )
        }
    }
}