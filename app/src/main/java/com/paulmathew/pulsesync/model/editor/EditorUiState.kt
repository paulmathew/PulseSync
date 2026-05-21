package com.paulmathew.pulsesync.model.editor

import com.paulmathew.pulsesync.model.editor.EditorSyncState

data class EditorUiState(
    val documentId: String,
    val title: String,
    val body: String,
    val updatedAtLabel: String,
    val syncState: EditorSyncState,
    val hasLocalChanges: Boolean
)