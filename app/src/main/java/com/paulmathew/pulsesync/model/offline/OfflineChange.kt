package com.paulmathew.pulsesync.model.offline

import com.paulmathew.pulsesync.model.sync.SyncStatus

data class OfflineChange(
    val id: String,
    val title: String,
    val subtitle: String,
    val updatedAtLabel: String,
    val status: SyncStatus
)