package com.paulmathew.pulsesync.ui.timeline

import com.paulmathew.pulsesync.model.SyncTimelineEvent
import com.paulmathew.pulsesync.model.TimelineEventSeverity

object TimelinePreviewData {

    val defaultState = TimelineUiState(
        selectedFilter = TimelineFilter.All,
        filters = listOf(
            TimelineFilter.All,
            TimelineFilter.Sync,
            TimelineFilter.Retry,
            TimelineFilter.Error,
            TimelineFilter.Conflict
        ),
        events = listOf(
            SyncTimelineEvent(
                timestamp = "12:00:10",
                title = "POST /notes/42",
                detail = "Synced successfully",
                severity = TimelineEventSeverity.Success
            ),
            SyncTimelineEvent(
                timestamp = "12:00:08",
                title = "POST /notes/41",
                detail = "Retry #2 succeeded",
                severity = TimelineEventSeverity.Success
            ),
            SyncTimelineEvent(
                timestamp = "12:00:05",
                title = "POST /notes/41",
                detail = "Retry #1 timed out",
                severity = TimelineEventSeverity.Warning
            ),
            SyncTimelineEvent(
                timestamp = "12:00:03",
                title = "POST /notes/41",
                detail = "Request timeout",
                severity = TimelineEventSeverity.Error
            ),
            SyncTimelineEvent(
                timestamp = "12:00:03",
                title = "Enqueued",
                detail = "POST /notes/41",
                severity = TimelineEventSeverity.Info
            ),
            SyncTimelineEvent(
                timestamp = "12:00:02",
                title = "Update Local",
                detail = "Note \"Offline design doc\"",
                severity = TimelineEventSeverity.Info
            ),
            SyncTimelineEvent(
                timestamp = "12:00:01",
                title = "Create Local",
                detail = "Note \"PulseSync idea\"",
                severity = TimelineEventSeverity.Info
            )
        )
    )
}