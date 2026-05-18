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
}