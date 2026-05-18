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
val TimelineFilter.label: String
    get() = when (this) {
        TimelineFilter.All -> "All"
        TimelineFilter.Sync -> "Sync"
        TimelineFilter.Retry -> "Retry"
        TimelineFilter.Error -> "Error"
        TimelineFilter.Conflict -> "Conflict"
    }