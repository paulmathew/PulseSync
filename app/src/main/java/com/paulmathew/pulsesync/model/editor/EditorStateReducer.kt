package com.paulmathew.pulsesync.model.editor

import com.paulmathew.pulsesync.model.editor.EditorIntent
import com.paulmathew.pulsesync.model.editor.EditorSyncState

object EditorStateReducer {

    fun reduce(
        state: EditorUiState,
        intent: EditorIntent
    ): EditorUiState {
        return when (intent) {
            is EditorIntent.TitleChanged -> {
                state.copy(
                    title = intent.value,
                    syncState = EditorSyncState.Syncing,
                    hasLocalChanges = true,
                    updatedAtLabel = "Saving locally..."
                )
            }

            is EditorIntent.BodyChanged -> {
                state.copy(
                    body = intent.value,
                    syncState = EditorSyncState.Syncing,
                    hasLocalChanges = true,
                    updatedAtLabel = "Saving locally..."
                )
            }

            EditorIntent.SaveAcknowledged -> {
                state.copy(
                    syncState = EditorSyncState.Synced,
                    hasLocalChanges = false,
                    updatedAtLabel = "Synced just now"
                )
            }

            EditorIntent.SyncStarted -> {
                state.copy(syncState = EditorSyncState.Syncing)
            }

            EditorIntent.WentOffline -> {
                state.copy(
                    syncState = EditorSyncState.OfflinePending,
                    updatedAtLabel = "Saved offline"
                )
            }

            EditorIntent.RetryStarted -> {
                state.copy(
                    syncState = EditorSyncState.Retrying,
                    updatedAtLabel = "Retrying sync..."
                )
            }
        }
    }
}