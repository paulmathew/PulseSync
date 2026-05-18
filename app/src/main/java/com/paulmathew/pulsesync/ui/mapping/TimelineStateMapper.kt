package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.model.SyncTimelineEvent
import com.paulmathew.pulsesync.model.TimelineEventSeverity
import com.paulmathew.pulsesync.sync.SyncEngineEvent
import com.paulmathew.pulsesync.sync.SyncFailureReason
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.timeline.TimelineFilter
import com.paulmathew.pulsesync.ui.timeline.TimelineUiState

fun SyncRuntimeState.toTimelineUiState(
    selectedFilter: TimelineFilter = TimelineFilter.All
): TimelineUiState {
    return TimelineUiState(
        selectedFilter = selectedFilter,
        filters = listOf(
            TimelineFilter.All,
            TimelineFilter.Sync,
            TimelineFilter.Retry,
            TimelineFilter.Error,
            TimelineFilter.Conflict
        ),
        events = events
            .filter { it.matches(selectedFilter) }
            .map { it.toSyncTimelineEvent() }
    )
}

internal fun SyncEngineEvent.toSyncTimelineEvent(): SyncTimelineEvent {
    return when (this) {
        is SyncEngineEvent.OperationStarted -> SyncTimelineEvent(
            timestamp = occurredAtMillis.toTimestampLabel(),
            title = "Sync started",
            detail = operationId,
            severity = TimelineEventSeverity.Info
        )

        is SyncEngineEvent.OperationSynced -> SyncTimelineEvent(
            timestamp = occurredAtMillis.toTimestampLabel(),
            title = "Sync completed",
            detail = operationId,
            severity = TimelineEventSeverity.Success
        )

        is SyncEngineEvent.OperationFailed -> SyncTimelineEvent(
            timestamp = occurredAtMillis.toTimestampLabel(),
            title = reason.title,
            detail = operationId,
            severity = reason.toSeverity()
        )

        is SyncEngineEvent.RetryScheduled -> SyncTimelineEvent(
            timestamp = occurredAtMillis.toTimestampLabel(),
            title = "Retry scheduled",
            detail = "$operationId · retry at ${nextRetryAtMillis.toTimestampLabel()}",
            severity = TimelineEventSeverity.Warning
        )
    }
}

private val SyncFailureReason.title: String
    get() = when (this) {
        SyncFailureReason.Timeout -> "Request timeout"
        SyncFailureReason.Offline -> "Offline"
        SyncFailureReason.ServerUnavailable -> "Server unavailable"
        SyncFailureReason.Conflict -> "Conflict detected"
        is SyncFailureReason.Unknown -> "Unknown failure"
    }

private fun SyncFailureReason.toSeverity(): TimelineEventSeverity {
    return when (this) {
        SyncFailureReason.Conflict -> TimelineEventSeverity.Warning
        SyncFailureReason.Offline -> TimelineEventSeverity.Warning
        SyncFailureReason.Timeout -> TimelineEventSeverity.Error
        SyncFailureReason.ServerUnavailable -> TimelineEventSeverity.Error
        is SyncFailureReason.Unknown -> TimelineEventSeverity.Error
    }
}

private fun Long.toTimestampLabel(): String {
    val totalSeconds = this / 1_000
    val minutes = (totalSeconds / 60) % 60
    val seconds = totalSeconds % 60

    return "%02d:%02d".format(minutes, seconds)
}
private fun SyncEngineEvent.matches(filter: TimelineFilter): Boolean {
    return when (filter) {
        TimelineFilter.All -> true
        TimelineFilter.Sync -> this is SyncEngineEvent.OperationStarted ||
                this is SyncEngineEvent.OperationSynced
        TimelineFilter.Retry -> this is SyncEngineEvent.RetryScheduled
        TimelineFilter.Error -> this is SyncEngineEvent.OperationFailed &&
                reason != SyncFailureReason.Conflict
        TimelineFilter.Conflict -> this is SyncEngineEvent.OperationFailed &&
                reason == SyncFailureReason.Conflict
    }
}