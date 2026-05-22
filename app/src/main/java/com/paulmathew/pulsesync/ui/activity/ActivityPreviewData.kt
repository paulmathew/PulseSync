package com.paulmathew.pulsesync.ui.activity

import com.paulmathew.pulsesync.model.activity.ActivityEvent
import com.paulmathew.pulsesync.model.activity.ActivityEventType
import com.paulmathew.pulsesync.model.activity.ActivityFeedUiState
import com.paulmathew.pulsesync.model.sync.SyncStatus

object ActivityPreviewData {

    val default = ActivityFeedUiState(
        today = listOf(
            ActivityEvent(
                id = "sarah-design-system",
                actorName = "Sarah",
                actorInitials = "SA",
                title = "Sarah edited Design System",
                subtitle = "Typography scale and color tokens updated",
                timestampLabel = "8:42 AM",
                type = ActivityEventType.Edit,
                syncStatus = SyncStatus.Synced
            ),
            ActivityEvent(
                id = "aurora-synced",
                actorName = "PulseSync",
                actorInitials = "PS",
                title = "Project Aurora synced",
                subtitle = "All local changes are now up to date",
                timestampLabel = "8:40 AM",
                type = ActivityEventType.Sync,
                syncStatus = SyncStatus.Synced
            ),
            ActivityEvent(
                id = "meeting-notes-created",
                actorName = "You",
                actorInitials = "PM",
                title = "You added Meeting Notes",
                subtitle = "Saved instantly on this device",
                timestampLabel = "7:21 AM",
                type = ActivityEventType.Create,
                syncStatus = SyncStatus.Syncing
            )
        ),
        yesterday = listOf(
            ActivityEvent(
                id = "offline-uploaded",
                actorName = "PulseSync",
                actorInitials = "PS",
                title = "Offline changes uploaded",
                subtitle = "3 saved changes synced after reconnecting",
                timestampLabel = "Yesterday, 9:11 PM",
                type = ActivityEventType.Offline,
                syncStatus = SyncStatus.Synced
            ),
            ActivityEvent(
                id = "conflict-resolved",
                actorName = "You",
                actorInitials = "PM",
                title = "Conflict resolved successfully",
                subtitle = "Project Aurora now has a single trusted version",
                timestampLabel = "Yesterday, 8:47 PM",
                type = ActivityEventType.Conflict,
                syncStatus = SyncStatus.Synced
            )
        ),
        earlier = emptyList()
    )
}