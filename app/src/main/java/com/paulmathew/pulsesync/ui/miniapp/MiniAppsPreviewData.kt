package com.paulmathew.pulsesync.ui.miniapp

import com.paulmathew.pulsesync.model.miniapp.MiniAppDefinition
import com.paulmathew.pulsesync.model.miniapp.MiniAppType

object MiniAppsPreviewData {

    val default = MiniAppsUiState(
        apps = listOf(
            MiniAppDefinition(
                id = "focus-session",
                title = "Focus Session",
                subtitle = "Shared notes, tasks, and presence for deep work.",
                statusLabel = "Available",
                type = MiniAppType.FocusSession
            ),
            MiniAppDefinition(
                id = "meeting-notes",
                title = "Meeting Notes",
                subtitle = "Collaborative notes that keep working offline.",
                statusLabel = "Coming soon",
                type = MiniAppType.MeetingNotes
            ),
            MiniAppDefinition(
                id = "shared-brainstorm",
                title = "Shared Brainstorm",
                subtitle = "Capture ideas together with local-first sync.",
                statusLabel = "Coming soon",
                type = MiniAppType.SharedBrainstorm
            ),
            MiniAppDefinition(
                id = "offline-journal",
                title = "Offline Journal",
                subtitle = "Private writing with calm sync confidence.",
                statusLabel = "Coming soon",
                type = MiniAppType.OfflineJournal
            )
        )
    )
}