package com.paulmathew.pulsesync.ui.home

import com.paulmathew.pulsesync.model.workspace.Collaborator
import com.paulmathew.pulsesync.model.workspace.CollaboratorTone
import com.paulmathew.pulsesync.model.workspace.WorkspaceItem
import com.paulmathew.pulsesync.model.workspace.WorkspaceSyncState

object WorkspaceHomePreviewData {

    val defaultState = WorkspaceHomeUiState(
        greeting = "My Workspace",
        lastSyncLabel = "Last synced just now",
        selectedFilter = WorkspaceFilter.All,
        filters = listOf(
            WorkspaceFilter.All,
            WorkspaceFilter.Notes,
            WorkspaceFilter.Tasks,
            WorkspaceFilter.Ideas,
            WorkspaceFilter.Files
        ),
        items = listOf(
            WorkspaceItem(
                id = "workspace-aurora",
                title = "Project Aurora",
                preview = "Brainstorming new sync architecture ideas and offline conflict resolution...",
                collaborators = listOf(
                    collaborator("sarah", "Sarah", "SA", CollaboratorTone.Rose),
                    collaborator("alex", "Alex", "AL", CollaboratorTone.Blue),
                    collaborator("you", "You", "PM", CollaboratorTone.Violet)
                ),
                lastModifiedLabel = "Updated 2m ago by you",
                syncState = WorkspaceSyncState.Synced,
                pendingLocalChanges = 0
            ),
            WorkspaceItem(
                id = "design-system",
                title = "Design System",
                preview = "Typography scale, color tokens and component guidelines.",
                collaborators = listOf(
                    collaborator("you", "You", "PM", CollaboratorTone.Violet),
                    collaborator("sarah", "Sarah", "SA", CollaboratorTone.Green)
                ),
                lastModifiedLabel = "Updated 5m ago by Sarah",
                syncState = WorkspaceSyncState.Syncing(pendingChanges = 2),
                pendingLocalChanges = 2
            ),
            WorkspaceItem(
                id = "meeting-notes",
                title = "Meeting Notes",
                preview = "Discussed roadmap, sync engine improvements and performance priorities...",
                collaborators = listOf(
                    collaborator("alex", "Alex", "AL", CollaboratorTone.Blue)
                ),
                lastModifiedLabel = "Updated 1h ago by Alex",
                syncState = WorkspaceSyncState.Offline(pendingChanges = 3),
                pendingLocalChanges = 3
            ),
            WorkspaceItem(
                id = "personal-journal",
                title = "Personal Journal",
                preview = "Daily reflection and thoughts...",
                collaborators = listOf(
                    collaborator("you", "You", "PM", CollaboratorTone.Violet)
                ),
                lastModifiedLabel = "Updated yesterday",
                syncState = WorkspaceSyncState.Synced,
                pendingLocalChanges = 0
            )
        )
    )

    private fun collaborator(
        id: String,
        displayName: String,
        initials: String,
        tone: CollaboratorTone
    ): Collaborator {
        return Collaborator(
            id = id,
            displayName = displayName,
            initials = initials,
            avatarTone = tone
        )
    }
}