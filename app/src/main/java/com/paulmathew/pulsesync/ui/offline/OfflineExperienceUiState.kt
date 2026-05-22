package com.paulmathew.pulsesync.ui.offline

import com.paulmathew.pulsesync.model.offline.OfflineChange

data class OfflineExperienceUiState(
    val title: String,
    val message: String,
    val changes: List<OfflineChange>
) {
    val pendingCount: Int
        get() = changes.size
}