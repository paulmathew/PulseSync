package com.paulmathew.pulsesync.ui.timeline

import com.paulmathew.pulsesync.model.SyncTimelineEvent

data class TimelineUiState(
    val selectedFilter: TimelineFilter,
    val filters: List<TimelineFilter>,
    val events: List<SyncTimelineEvent>
)

enum class TimelineFilter {
    All,
    Sync,
    Retry,
    Error,
    Conflict
}