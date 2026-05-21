package com.paulmathew.pulsesync.ui.editor

import com.paulmathew.pulsesync.model.editor.EditorSyncState
import com.paulmathew.pulsesync.model.editor.EditorUiState
import com.paulmathew.pulsesync.ui.editor.EditorPreviewData.designSystem
import com.paulmathew.pulsesync.ui.editor.EditorPreviewData.meetingNotes
import com.paulmathew.pulsesync.ui.editor.EditorPreviewData.projectAurora

object EditorPreviewData {

    val projectAurora = EditorUiState(
        documentId = "project-aurora",
        title = "Offline-first is a feature, not a fallback.",
        body = """
            Great products should work beautifully, even when the network doesn’t.

            PulseSync makes collaboration feel instant by applying changes locally first, then reconciling them through a deterministic sync pipeline.

            The user stays in flow. The system handles uncertainty quietly.
        """.trimIndent(),
        updatedAtLabel = "Synced just now",
        syncState = EditorSyncState.Synced,
        hasLocalChanges = false
    )
    val designSystem = EditorUiState(
        documentId = "design-system",
        title = "Design System",
        body = """
        Typography scale, color tokens and component guidelines.

        This document captures the visual foundation for PulseSync v2.
    """.trimIndent(),
        updatedAtLabel = "Syncing",
        syncState = EditorSyncState.Syncing,
        hasLocalChanges = true
    )

    val meetingNotes = EditorUiState(
        documentId = "meeting-notes",
        title = "Meeting Notes",
        body = """
        Discussed roadmap, sync engine improvements and performance priorities.

        Next focus is document routing, editor polish and queue visibility.
    """.trimIndent(),
        updatedAtLabel = "Saved offline",
        syncState = EditorSyncState.OfflinePending,
        hasLocalChanges = true
    )
    fun documentFor(documentId: String?): EditorUiState {
        return when (documentId) {
            "project-aurora" -> projectAurora
            "design-system" -> designSystem
            "meeting-notes" -> meetingNotes
            else -> projectAurora
        }
    }
}
