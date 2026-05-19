package com.paulmathew.pulsesync.sync.conflict

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConflictResolverTest {

    @Test
    fun resolve_localWins_returnsLocalVersion() {
        val conflict = conflict()

        val result = ConflictResolver.resolve(
            conflict = conflict,
            strategy = ConflictResolutionStrategy.LocalWins,
            resolvedAtMillis = 5_000
        )

        assertTrue(result is ConflictResolutionResult.Resolved)

        result as ConflictResolutionResult.Resolved
        assertEquals("local-v1", result.resolvedVersion.versionId)
        assertEquals(5_000, result.resolvedVersion.updatedAtMillis)
    }

    @Test
    fun resolve_remoteWins_returnsRemoteVersion() {
        val conflict = conflict()

        val result = ConflictResolver.resolve(
            conflict = conflict,
            strategy = ConflictResolutionStrategy.RemoteWins,
            resolvedAtMillis = 5_000
        )

        assertTrue(result is ConflictResolutionResult.Resolved)

        result as ConflictResolutionResult.Resolved
        assertEquals("remote-v1", result.resolvedVersion.versionId)
        assertEquals(5_000, result.resolvedVersion.updatedAtMillis)
    }

    @Test
    fun resolve_merge_returnsMergedVersion() {
        val conflict = conflict()

        val result = ConflictResolver.resolve(
            conflict = conflict,
            strategy = ConflictResolutionStrategy.Merge(
                mergedSummary = "Merged offline note",
                mergedPayloadHash = "hash-merged"
            ),
            resolvedAtMillis = 5_000
        )

        assertTrue(result is ConflictResolutionResult.Resolved)

        result as ConflictResolutionResult.Resolved
        assertEquals("merged-op-1", result.resolvedVersion.versionId)
        assertEquals("hash-merged", result.resolvedVersion.payloadHash)
        assertEquals("Merged offline note", result.resolvedVersion.summary)
    }

    @Test
    fun resolve_manualReview_returnsRequiresManualReview() {
        val conflict = conflict()

        val result = ConflictResolver.resolve(
            conflict = conflict,
            strategy = ConflictResolutionStrategy.ManualReview,
            resolvedAtMillis = 5_000
        )

        assertTrue(result is ConflictResolutionResult.RequiresManualReview)
    }

    private fun conflict(): SyncConflict {
        return SyncConflict(
            operationId = "op-1",
            resourcePath = "/notes/41",
            detectedAtMillis = 1_000,
            localVersion = ConflictingVersion(
                versionId = "local-v1",
                updatedAtMillis = 2_000,
                payloadHash = "hash-local",
                summary = "Local offline edit"
            ),
            remoteVersion = ConflictingVersion(
                versionId = "remote-v1",
                updatedAtMillis = 3_000,
                payloadHash = "hash-remote",
                summary = "Remote server edit"
            )
        )
    }
}