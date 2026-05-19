package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.model.NetworkProfileType
import com.paulmathew.pulsesync.sync.RetryPolicy
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationMethod
import com.paulmathew.pulsesync.sync.SyncOperationStatus
import com.paulmathew.pulsesync.sync.conflict.ConflictResolutionResult
import com.paulmathew.pulsesync.sync.conflict.ConflictResolutionStrategy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeSyncOrchestratorTest {

    @Test
    fun enqueue_addsOperationToRuntimeState() {
        val orchestrator = FakeSyncOrchestrator()

        orchestrator.enqueue(pendingOperation("op-1"))

        assertEquals(1, orchestrator.state.value.operations.size)
    }

    @Test
    fun startNext_marksFirstPendingOperationInFlight() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))

        orchestrator.startNext(nowMillis = 1_000)

        val operation = orchestrator.state.value.operations.single()
        assertEquals(SyncOperationStatus.InFlight, operation.status)
        assertEquals("op-1", orchestrator.state.value.activeOperationId)
    }

    @Test
    fun startNext_doesNothingWhenOperationAlreadyActive() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.enqueue(pendingOperation("op-2"))

        orchestrator.startNext(nowMillis = 1_000)
        orchestrator.startNext(nowMillis = 2_000)

        assertEquals("op-1", orchestrator.state.value.activeOperationId)
        assertEquals(
            SyncOperationStatus.Pending,
            orchestrator.state.value.operations.first { it.id == "op-2" }.status
        )
    }

    @Test
    fun completeActive_successMarksOperationSyncedAndClearsActiveOperation() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Success,
            nowMillis = 2_000
        )

        val operation = orchestrator.state.value.operations.single()
        assertEquals(SyncOperationStatus.Synced, operation.status)
        assertNull(orchestrator.state.value.activeOperationId)
    }

    @Test
    fun completeActive_failureSchedulesRetryEvent() {
        val orchestrator = FakeSyncOrchestrator(
            retryPolicy = RetryPolicy(
                maxAttempts = 3,
                baseDelayMillis = 1_000,
                maxDelayMillis = 30_000
            )
        )
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Timeout),
            nowMillis = 2_000
        )

        val operation = orchestrator.state.value.operations.single()
        assertTrue(operation.status is SyncOperationStatus.Failed)
        assertNull(orchestrator.state.value.activeOperationId)
        assertEquals(3, orchestrator.state.value.events.size)
    }

    @Test
    fun startNext_doesNotRetryFailedOperationBeforeRetryTime() {
        val orchestrator = FakeSyncOrchestrator(
            retryPolicy = RetryPolicy(
                maxAttempts = 3,
                baseDelayMillis = 1_000,
                maxDelayMillis = 30_000
            )
        )
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Timeout),
            nowMillis = 2_000
        )

        orchestrator.startNext(nowMillis = 2_500)

        assertNull(orchestrator.state.value.activeOperationId)
    }

    @Test
    fun startNext_retriesFailedOperationAtRetryTime() {
        val orchestrator = FakeSyncOrchestrator(
            retryPolicy = RetryPolicy(
                maxAttempts = 3,
                baseDelayMillis = 1_000,
                maxDelayMillis = 30_000
            )
        )
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Timeout),
            nowMillis = 2_000
        )

        orchestrator.startNext(nowMillis = 4_000)

        assertEquals("op-1", orchestrator.state.value.activeOperationId)
        assertEquals(
            SyncOperationStatus.InFlight,
            orchestrator.state.value.operations.single().status
        )
    }

    @Test
    fun completeActive_conflictFailureAddsRuntimeConflict() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = 2_000
        )

        assertEquals(1, orchestrator.state.value.conflicts.size)
        assertEquals("op-1", orchestrator.state.value.conflicts.single().operationId)
    }

    @Test
    fun completeActive_duplicateConflictFailureReplacesExistingConflict() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = 2_000
        )

        orchestrator.startNext(nowMillis = 4_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = 5_000
        )

        assertEquals(1, orchestrator.state.value.conflicts.size)
        assertEquals(5_000, orchestrator.state.value.conflicts.single().detectedAtMillis)
    }

    @Test
    fun resolveConflict_localWinsRemovesConflict() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = 2_000
        )

        val result = orchestrator.resolveConflict(
            operationId = "op-1",
            strategy = ConflictResolutionStrategy.LocalWins,
            resolvedAtMillis = 3_000
        )

        assertTrue(result is ConflictResolutionResult.Resolved)
        assertTrue(orchestrator.state.value.conflicts.isEmpty())
    }

    @Test
    fun resolveConflict_remoteWinsRemovesConflict() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = 2_000
        )

        val result = orchestrator.resolveConflict(
            operationId = "op-1",
            strategy = ConflictResolutionStrategy.RemoteWins,
            resolvedAtMillis = 3_000
        )

        assertTrue(result is ConflictResolutionResult.Resolved)
        assertTrue(orchestrator.state.value.conflicts.isEmpty())
    }

    @Test
    fun resolveConflict_manualReviewKeepsConflictActive() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)

        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Conflict),
            nowMillis = 2_000
        )

        val result = orchestrator.resolveConflict(
            operationId = "op-1",
            strategy = ConflictResolutionStrategy.ManualReview,
            resolvedAtMillis = 3_000
        )

        assertTrue(result is ConflictResolutionResult.RequiresManualReview)
        assertEquals(1, orchestrator.state.value.conflicts.size)
    }

    @Test
    fun resolveConflict_returnsNullWhenConflictDoesNotExist() {
        val orchestrator = FakeSyncOrchestrator()

        val result = orchestrator.resolveConflict(
            operationId = "missing",
            strategy = ConflictResolutionStrategy.LocalWins,
            resolvedAtMillis = 3_000
        )

        assertNull(result)
    }

    private fun pendingOperation(id: String): SyncOperation {
        return SyncOperation(
            id = id,
            method = SyncOperationMethod.Create,
            resourcePath = "/notes/$id",
            payloadHash = "hash-$id",
            createdAtMillis = 0,
            attemptCount = 0,
            status = SyncOperationStatus.Pending
        )
    }
    @Test
    fun selectNetworkProfile_updatesRuntimeNetworkProfile() {
        val orchestrator = FakeSyncOrchestrator()
        val profile = NetworkProfiles.all.first {
            it.type == NetworkProfileType.Timeout
        }

        orchestrator.selectNetworkProfile(profile)

        assertEquals(profile, orchestrator.state.value.networkSimulation.selectedProfile)
    }
}