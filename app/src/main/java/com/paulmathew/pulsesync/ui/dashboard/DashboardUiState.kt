package com.paulmathew.pulsesync.ui.dashboard

import com.paulmathew.pulsesync.model.SyncHealthStatus
import com.paulmathew.pulsesync.model.SyncMetric
import com.paulmathew.pulsesync.model.SyncTimelineEvent

data class DashboardUiState(
    val healthStatus: SyncHealthStatus,
    val healthLabel: String,
    val healthDescription: String,
    val syncProgressPercent: Int,
    val metrics: List<SyncMetric>,
    val recentEvents: List<SyncTimelineEvent>,
    val lastSyncLabel: String,
    val nextAttemptLabel: String
)