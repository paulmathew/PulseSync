package com.paulmathew.pulsesync.model.workspace

data class WorkspaceItem(
    val id: String,
    val title: String,
    val preview: String,
    val collaborators: List<Collaborator>,
    val lastModifiedLabel: String,
    val syncState: WorkspaceSyncState,
    val pendingLocalChanges: Int
)