package com.paulmathew.pulsesync.ui.queue.v2

import com.paulmathew.pulsesync.model.queue.SyncQueueItem
import com.paulmathew.pulsesync.model.sync.SyncStatus

object SyncQueuePreviewData {

    val default = SyncQueueDrawerUiState(
        uploading = listOf(
            SyncQueueItem(
                id = "upload-project-aurora",
                title = "Update Project Aurora",
                subtitle = "Uploading latest editor changes",
                timestampLabel = "Just now",
                payloadLabel = "2.4 KB",
                status = SyncStatus.Syncing
            )
        ),
        pending = listOf(
            SyncQueueItem(
                id = "pending-design-system",
                title = "Edit Design System",
                subtitle = "Saved locally and waiting for network",
                timestampLabel = "5m ago",
                payloadLabel = "1.8 KB",
                status = SyncStatus.OfflinePending(pendingChanges = 1)
            )
        ),
        retrying = listOf(
            SyncQueueItem(
                id = "retry-roadmap",
                title = "Update Roadmap",
                subtitle = "Connection failed. Retrying quietly.",
                timestampLabel = "12m ago",
                payloadLabel = "3.1 KB",
                status = SyncStatus.Retrying("Retrying in 8s")
            )
        ),
        synced = listOf(
            SyncQueueItem(
                id = "synced-meeting-notes",
                title = "Meeting Notes",
                subtitle = "Changes synced successfully",
                timestampLabel = "18m ago",
                payloadLabel = "900 B",
                status = SyncStatus.Synced
            )
        )
    )
}