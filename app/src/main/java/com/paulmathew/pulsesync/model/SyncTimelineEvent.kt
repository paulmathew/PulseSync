package com.paulmathew.pulsesync.model

data class SyncTimelineEvent(
    val timestamp: String,
    val title: String,
    val detail: String,
    val severity: TimelineEventSeverity
)