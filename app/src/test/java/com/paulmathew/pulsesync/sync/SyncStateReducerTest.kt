package com.paulmathew.pulsesync.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SyncStateReducerTest {

    @Test
    fun markInFlight_setsOperationInFlightAndEmitsStartedEvent() {
        val operation = pendingOperation()

        val transition = SyncStateReducer.markInFlight(
            operation = operation,
            nowMillis = 1_000L
        )

        assertEquals(SyncOperationStatus.InFlight, transition.next.status)
        assertTrue(transition.event is SyncEngineEvent.OperationStarted)
    }

    @Test
    fun applyAttemptResult_success_marksOperationSynced() {
        val operation = pendingOperation().copy(
            status = SyncOperationStatus.InFlight
        )

        val transition = SyncStateReducer.applyAttemptResult(
            operation = operation,
            result = SyncAttemptResult.Success,
            retryPolicy = RetryPolicy.Default,
            nowMillis = 2_000L
        )

        assertEquals(SyncOperationStatus.Synced, transition.next.status)
        assertTrue(transition.event is SyncEngineEvent.OperationSynced)
    }

    @Test
    fun applyAttemptResult_failure_marksFailedAndSchedulesRetry() {
        val operation = pendingOperation().copy(
            status = SyncOperationStatus.InFlight,
            attemptCount = 0
        )

        val transition = SyncStateReducer.applyAttemptResult(
            operation = operation,
            result = SyncAttemptResult.Failure(SyncFailureReason.Timeout),
            retryPolicy = RetryPolicy.Default,
            nowMillis = 10_000L
        )

        assertEquals(1, transition.next.attemptCount)

        val status = transition.next.status
        assertTrue(status is SyncOperationStatus.Failed)

        status as SyncOperationStatus.Failed
        assertEquals(SyncFailureReason.Timeout, status.reason)
        // RetryPolicy.Default has baseDelay 1000. nextAttemptCount is 1.
        // delay = 1000 * 2^1 = 2000.
        // nextRetryAtMillis = 10_000 + 2_000 = 12_000.
        assertEquals(12_000L, status.nextRetryAtMillis)
    }

    private fun pendingOperation(): SyncOperation {
        return SyncOperation(
            id = "op-test",
            method = SyncOperationMethod.Create,
            resourcePath = "/notes/test",
            payloadHash = "hash-test",
            createdAtMillis = 0L,
            attemptCount = 0,
            status = SyncOperationStatus.Pending
        )
    }
}
