package com.paulmathew.pulsesync.model.focus

import com.paulmathew.pulsesync.model.sync.SyncStatus

data class FocusSession(
    val id: String,
    val title: String,
    val subtitle: String,
    val collaborators: List<FocusCollaborator>,
    val items: List<FocusItem>,
    val activity: List<FocusActivity>,
    val syncStatus: SyncStatus
)

data class FocusCollaborator(
    val id: String,
    val name: String,
    val initials: String,
    val isActive: Boolean
)

data class FocusItem(
    val id: String,
    val title: String,
    val detail: String,
    val type: FocusItemType,
    val syncStatus: SyncStatus
)

enum class FocusItemType {
    Note,
    Task
}

data class FocusActivity(
    val id: String,
    val message: String,
    val timestampLabel: String
)