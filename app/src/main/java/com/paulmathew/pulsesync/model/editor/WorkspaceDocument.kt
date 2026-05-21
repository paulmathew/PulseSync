package com.paulmathew.pulsesync.model.editor

data class WorkspaceDocument(
    val id: String,
    val title: String,
    val body: String,
    val updatedAtLabel: String,
    val syncState: EditorSyncState
)