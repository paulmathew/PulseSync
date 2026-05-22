package com.paulmathew.pulsesync.ui.offline

import com.paulmathew.pulsesync.model.offline.OfflineChange
import com.paulmathew.pulsesync.model.sync.SyncStatus

object OfflineExperiencePreviewData {

    val default = OfflineExperienceUiState(
        title = "You're offline",
        message = "No worries, keep working. Changes will sync when you're back online.",
        changes = listOf(
            OfflineChange(
                id = "offline-project-aurora",
                title = "Project Aurora",
                subtitle = "Edited locally",
                updatedAtLabel = "2m ago",
                status = SyncStatus.OfflinePending(pendingChanges = 1)
            ),
            OfflineChange(
                id = "offline-personal-journal",
                title = "Personal Journal",
                subtitle = "Saved on this device",
                updatedAtLabel = "1h ago",
                status = SyncStatus.OfflinePending(pendingChanges = 1)
            ),
            OfflineChange(
                id = "offline-ideas",
                title = "Ideas - Voice Notes",
                subtitle = "Waiting for connection",
                updatedAtLabel = "3h ago",
                status = SyncStatus.OfflinePending(pendingChanges = 1)
            )
        )
    )
}