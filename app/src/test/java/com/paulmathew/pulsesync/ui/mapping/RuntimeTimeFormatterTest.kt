package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.model.SyncHealthStatus
import com.paulmathew.pulsesync.model.TimelineEventSeverity
import com.paulmathew.pulsesync.model.QueuedOperationStatus
import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.SyncOperation
import com.paulmathew.pulsesync.sync.SyncOperationMethod
import com.paulmathew.pulsesync.sync.SyncOperationStatus
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.queue.QueueFilter
import com.paulmathew.pulsesync.ui.timeline.TimelineFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RuntimeStateMapperTest {

    @Test
    fun toDashboardUiState_countsOperationStatuses() {
        val state = SyncRuntimeState(
            operations = listOf(
                operation("pending", SyncOperationStatus.Pending),
                operation("syncing", SyncOperationStatus.InFlight),
                operation("failed", SyncOperationStatus.Failed(SyncFailureReason.Timeout, 10_000)),
                operation("synced", SyncOperationStatus.Synced)
            ),
            events = emptyList(),
            activeOperationId = "syncing",
            conflicts = emptyList(),
            )

        val dashboardState = state.toDashboardUiState()

        assertEquals(SyncHealthStatus.Degraded, dashboardState.healthStatus)
        assertEquals("1", dashboardState.metrics.first { it.label == "Pending" }.value)
        assertEquals("1", dashboardState.metrics.first { it.label == "Syncing" }.value)
        assertEquals("1", dashboardState.metrics.first { it.label == "Failed" }.value)
        assertEquals("1", dashboardState.metrics.first { it.label == "Synced" }.value)
    }

    @Test
    fun toTimelineUiState_mapsFailureEventSeverity() {
        val state = SyncRuntimeState(
            operations = emptyList(),
            events = listOf(
                SyncEngineEvent.OperationFailed(
                    operationId = "op-1",
                    occurredAtMillis = 1_000,
                    reason = SyncFailureReason.Timeout,
                    nextRetryAtMillis = 2_000
                )
            ),
            activeOperationId = null,
            conflicts = emptyList(),

        )

        val timelineState = state.toTimelineUiState()

        assertEquals(1, timelineState.events.size)
        assertEquals(TimelineEventSeverity.Error, timelineState.events.single().severity)
    }

    @Test
    fun toQueueUiState_excludesSyncedAndCancelledOperations() {
        val state = SyncRuntimeState(
            operations = listOf(
                operation("pending", SyncOperationStatus.Pending),
                operation("synced", SyncOperationStatus.Synced),
                operation("cancelled", SyncOperationStatus.Cancelled)
            ),
            events = emptyList(),
            activeOperationId = null,
            conflicts = emptyList(),
        )

        val queueState = state.toQueueUiState()

        assertEquals(1, queueState.operations.size)
        assertEquals(QueuedOperationStatus.Pending, queueState.operations.single().status)
    }

    private fun operation(
        id: String,
        status: SyncOperationStatus
    ): SyncOperation {
        return SyncOperation(
            id = id,
            method = SyncOperationMethod.Create,
            resourcePath = "/notes/$id",
            payloadHash = "hash-$id",
            createdAtMillis = 0,
            attemptCount = 0,
            status = status
        )
    }
    @Test
    fun toQueueUiState_filtersFailedOperations() {
        val state = SyncRuntimeState(
            operations = listOf(
                operation("pending", SyncOperationStatus.Pending),
                operation("failed", SyncOperationStatus.Failed(SyncFailureReason.Timeout, 10_000)),
                operation("syncing", SyncOperationStatus.InFlight)
            ),
            events = emptyList(),
            activeOperationId = null,
            conflicts = emptyList(),
        )

        val queueState = state.toQueueUiState(selectedFilter = QueueFilter.Failed)

        assertEquals(1, queueState.operations.size)
        assertEquals(QueuedOperationStatus.Failed, queueState.operations.single().status)
    }
    @Test
    fun toTimelineUiState_filtersRetryEvents() {
        val state = SyncRuntimeState(
            operations = emptyList(),
            events = listOf(
                SyncEngineEvent.OperationStarted(
                    operationId = "op-1",
                    occurredAtMillis = 1_000
                ),
                SyncEngineEvent.RetryScheduled(
                    operationId = "op-1",
                    occurredAtMillis = 2_000,
                    nextRetryAtMillis = 5_000
                )
            ),
            activeOperationId = null,
            conflicts = emptyList(),
        )

        val timelineState = state.toTimelineUiState(selectedFilter = TimelineFilter.Retry)

        assertEquals(1, timelineState.events.size)
        assertEquals("Retry scheduled", timelineState.events.single().title)
    }
    @Test
    fun toObservabilityUiState_calculatesSuccessRateAndFailureReasons() {
        val state = SyncRuntimeState(
            operations = emptyList(),
            events = listOf(
                SyncEngineEvent.OperationSynced("op-1", 1_000),
                SyncEngineEvent.OperationFailed("op-2", 2_000, SyncFailureReason.Timeout, 3_000),
                SyncEngineEvent.RetryScheduled("op-2", 2_000, 3_000)
            ),
            activeOperationId = null,
            conflicts = emptyList(),
        )

        val observabilityState = state.toObservabilityUiState()

        assertEquals("50%", observabilityState.successRateLabel)
        assertEquals(1, observabilityState.failureCount)
        assertEquals(1, observabilityState.retryScheduledCount)
        assertEquals("Timeout", observabilityState.failureReasons.single().label)
    }
}