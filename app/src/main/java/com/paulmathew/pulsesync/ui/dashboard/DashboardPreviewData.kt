package com.paulmathew.pulsesync.ui.dashboard

import com.paulmathew.pulsesync.model.OperationStatus
import com.paulmathew.pulsesync.model.SyncHealthStatus
import com.paulmathew.pulsesync.model.SyncMetric
import com.paulmathew.pulsesync.model.SyncTimelineEvent
import com.paulmathew.pulsesync.model.TimelineEventSeverity

object DashboardPreviewData {

    val healthyState = DashboardUiState(
        healthStatus = SyncHealthStatus.Healthy,
        healthLabel = "Healthy",
        healthDescription = "All sync systems operational",
        syncProgressPercent = 98,
        metrics = listOf(
            SyncMetric("Pending", "12", OperationStatus.Pending),
            SyncMetric("Syncing", "3", OperationStatus.Syncing),
            SyncMetric("Failed", "1", OperationStatus.Failed),
            SyncMetric("Synced", "1280", OperationStatus.Synced)
        ),
        recentEvents = listOf(
            SyncTimelineEvent("12:00:10", "POST /notes/42", "Synced successfully", TimelineEventSeverity.Success),
            SyncTimelineEvent("12:00:08", "POST /notes/41", "Retry #2 succeeded", TimelineEventSeverity.Success),
            SyncTimelineEvent("12:00:05", "POST /notes/41", "Retry #1 timed out", TimelineEventSeverity.Warning),
            SyncTimelineEvent("12:00:03", "POST /notes/41", "Request timeout", TimelineEventSeverity.Error)
        ),
        lastSyncLabel = "2s ago",
        nextAttemptLabel = "In 15s"
    )

    val degradedState = DashboardUiState(
        healthStatus = SyncHealthStatus.Degraded,
        healthLabel = "Degraded",
        healthDescription = "Retries increasing under poor network conditions",
        syncProgressPercent = 74,
        metrics = listOf(
            SyncMetric("Pending", "48", OperationStatus.Pending),
            SyncMetric("Syncing", "6", OperationStatus.Syncing),
            SyncMetric("Failed", "7", OperationStatus.Failed),
            SyncMetric("Synced", "934", OperationStatus.Synced)
        ),
        recentEvents = listOf(
            SyncTimelineEvent("12:04:18", "PUT /notes/58", "Retry scheduled in 15s", TimelineEventSeverity.Warning),
            SyncTimelineEvent("12:04:11", "POST /attachments/12", "Upload latency exceeded threshold", TimelineEventSeverity.Warning),
            SyncTimelineEvent("12:04:02", "POST /notes/57", "Synced after retry", TimelineEventSeverity.Success),
            SyncTimelineEvent("12:03:54", "Network profile changed", "Slow network simulation active", TimelineEventSeverity.Info)
        ),
        lastSyncLabel = "21s ago",
        nextAttemptLabel = "In 15s"
    )

    val failureHeavyState = DashboardUiState(
        healthStatus = SyncHealthStatus.Failing,
        healthLabel = "Failing",
        healthDescription = "Sync pipeline blocked by repeated timeouts",
        syncProgressPercent = 41,
        metrics = listOf(
            SyncMetric("Pending", "126", OperationStatus.Pending),
            SyncMetric("Syncing", "1", OperationStatus.Syncing),
            SyncMetric("Failed", "32", OperationStatus.Failed),
            SyncMetric("Synced", "611", OperationStatus.Synced)
        ),
        recentEvents = listOf(
            SyncTimelineEvent("12:08:42", "POST /notes/91", "Request timeout", TimelineEventSeverity.Error),
            SyncTimelineEvent("12:08:25", "POST /notes/90", "Retry budget exhausted", TimelineEventSeverity.Error),
            SyncTimelineEvent("12:08:10", "DELETE /notes/88", "Queued until connectivity recovers", TimelineEventSeverity.Warning),
            SyncTimelineEvent("12:07:58", "Network profile changed", "Timeout simulation active", TimelineEventSeverity.Info)
        ),
        lastSyncLabel = "3m ago",
        nextAttemptLabel = "Paused"
    )
}