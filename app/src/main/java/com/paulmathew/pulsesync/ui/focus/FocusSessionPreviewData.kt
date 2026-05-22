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
                ),
                FocusItem(
                    id = "item-3",
                    title = "Review sync confidence states",
                    detail = "Keep status visible but never distracting.",
                    type = FocusItemType.Task,
                    syncStatus = SyncStatus.Syncing
                ),
                FocusItem(
                    id = "item-4",
                    title = "Capture launch risks",
                    detail = "Note offline edge cases before the demo.",
                    type = FocusItemType.Note,
                    syncStatus = SyncStatus.Synced
                )

            ),
            activity = listOf(
                FocusActivity("a1", "Sarah is editing launch copy", "Just now"),
                FocusActivity("a2", "Offline changes synced", "2m ago"),
                FocusActivity("a3", "You added “Shape onboarding flow”", "5m ago"),
                FocusActivity("a4", "Alex reviewed offline behavior", "12m ago")
            ),
            syncStatus = SyncStatus.Syncing
        )
    )
}