package com.paulmathew.pulsesync.ui.queue.v2

import com.paulmathew.pulsesync.model.queue.SyncQueueItem

data class SyncQueueDrawerUiState(
    val uploading: List<SyncQueueItem>,
    val pending: List<SyncQueueItem>,
    val retrying: List<SyncQueueItem>,
    val synced: List<SyncQueueItem>
) {
    val totalActiveItems: Int
        get() = uploading.size + pending.size + retrying.size
}