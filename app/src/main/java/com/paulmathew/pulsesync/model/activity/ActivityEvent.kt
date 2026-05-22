package com.paulmathew.pulsesync.model.activity

import com.paulmathew.pulsesync.model.sync.SyncStatus

data class ActivityEvent(
    val id: String,
    val actorName: String,
    val actorInitials: String,
    val title: String,
    val subtitle: String,
    val timestampLabel: String,
    val type: ActivityEventType,
    val syncStatus: SyncStatus? = null
)

enum class ActivityEventType {
    Edit,
    Create,
    Sync,
    Offline,
    Conflict,
    Member
}