package com.paulmathew.pulsesync.ui.focus

import com.paulmathew.pulsesync.model.focus.FocusActivity
import com.paulmathew.pulsesync.model.focus.FocusCollaborator
import com.paulmathew.pulsesync.model.focus.FocusItem
import com.paulmathew.pulsesync.model.focus.FocusItemType
import com.paulmathew.pulsesync.model.focus.FocusSession
import com.paulmathew.pulsesync.model.focus.FocusSessionUiState
import com.paulmathew.pulsesync.model.sync.SyncStatus

object FocusSessionPreviewData {

    val default = FocusSessionUiState(
        session = FocusSession(
            id = "focus-launch",
            title = "Launch Focus",
            subtitle = "Shared planning space · working offline-first",
            collaborators = listOf(
                FocusCollaborator("paul", "Paul", "PM", true),
                FocusCollaborator("sarah", "Sarah", "SA", true),
                FocusCollaborator("alex", "Alex", "AL", false)
            ),
            items = listOf(
                FocusItem(
                    id = "item-1",
                    title = "Shape onboarding flow",
                    detail = "Keep the first session calm and fast.",
                    type = FocusItemType.Task,
                    syncStatus = SyncStatus.Synced
                ),
                FocusItem(
                    id = "item-2",
                    title = "Offline reassurance copy",
                    detail = "No scary network language.",
                    type = FocusItemType.Note,
                    syncStatus = SyncStatus.OfflinePending(1)
                )
            ),
            activity = listOf(
                FocusActivity("a1", "Sarah joined the session", "Just now"),
                FocusActivity("a2", "You added a focus task", "2m ago")
            ),
            syncStatus = SyncStatus.Syncing
        )
    )
}