package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.observability.FailureReasonMetric
import com.paulmathew.pulsesync.ui.observability.ObservabilityUiState

fun SyncRuntimeState.toObservabilityUiState(): ObservabilityUiState {
    val successCount = events.count { it is SyncEngineEvent.OperationSynced }
    val failureEvents = events.filterIsInstance<SyncEngineEvent.OperationFailed>()
    val retryScheduledCount = events.count { it is SyncEngineEvent.RetryScheduled }

    val completedCount = successCount + failureEvents.size
    val successRate = if (completedCount == 0) {
        0
    } else {
        ((successCount.toFloat() / completedCount) * 100).toInt()
    }

    return ObservabilityUiState(
        totalEvents = events.size,
        successCount = successCount,
        failureCount = failureEvents.size,
        retryScheduledCount = retryScheduledCount,
        successRateLabel = "$successRate%",
        failureReasons = failureEvents
            .groupingBy { it.reason.label }
            .eachCount()
            .map { (label, count) ->
                FailureReasonMetric(
                    label = label,
                    count = count
                )
            }
            .sortedByDescending { it.count }
    )
}

private val SyncFailureReason.label: String
    get() = when (this) {
        SyncFailureReason.Timeout -> "Timeout"
        SyncFailureReason.Offline -> "Offline"
        SyncFailureReason.ServerUnavailable -> "Server unavailable"
        SyncFailureReason.Conflict -> "Conflict"
        is SyncFailureReason.Unknown -> "Unknown"
    }