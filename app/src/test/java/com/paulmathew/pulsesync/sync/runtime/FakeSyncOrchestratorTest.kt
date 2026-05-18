package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.sync.RetryPolicy
import com.paulmathew.pulsesync.sync.SyncAttemptResult
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationMethod
import com.paulmathew.pulsesync.sync.SyncOperationStatus
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
        val orchestrator = FakeSyncOrchestrator()
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
    fun startNext_retriesFailedOperationAfterRetryTime() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.startNext(nowMillis = 1_000)
        orchestrator.completeActive(
            result = SyncAttemptResult.Failure(SyncFailureReason.Timeout),
            nowMillis = 2_000
        )

        // nextRetryAtMillis will be 4_000 (2_000 + 2_000 backoff)
        orchestrator.startNext(nowMillis = 4_500)

        assertEquals("op-1", orchestrator.state.value.activeOperationId)
        assertEquals(SyncOperationStatus.InFlight, orchestrator.state.value.operations.single().status)
    }

    @Test
    fun startNext_doesNotStartMultipleOperationsInParallel() {
        val orchestrator = FakeSyncOrchestrator()
        orchestrator.enqueue(pendingOperation("op-1"))
        orchestrator.enqueue(pendingOperation("op-2"))

        orchestrator.startNext(nowMillis = 1_000)
        assertEquals("op-1", orchestrator.state.value.activeOperationId)

        orchestrator.startNext(nowMillis = 1_500)
        assertEquals("op-1", orchestrator.state.value.activeOperationId)
        assertEquals(SyncOperationStatus.Pending, orchestrator.state.value.operations[1].status)
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
}