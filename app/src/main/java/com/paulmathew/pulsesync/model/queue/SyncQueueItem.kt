package com.paulmathew.pulsesync.model.queue

import com.paulmathew.pulsesync.model.sync.SyncStatus

data class SyncQueueItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val timestampLabel: String,
    val payloadLabel: String,
    val status: SyncStatus
)