package com.paulmathew.pulsesync.model.activity

import com.paulmathew.pulsesync.model.activity.ActivityEvent

data class ActivityFeedUiState(
    val today: List<ActivityEvent>,
    val yesterday: List<ActivityEvent>,
    val earlier: List<ActivityEvent>
) {
    val isEmpty: Boolean
        get() = today.isEmpty() && yesterday.isEmpty() && earlier.isEmpty()
}