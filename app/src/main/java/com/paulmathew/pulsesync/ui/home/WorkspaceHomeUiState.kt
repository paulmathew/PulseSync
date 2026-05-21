package com.paulmathew.pulsesync.ui.home

import com.paulmathew.pulsesync.model.workspace.WorkspaceItem

data class WorkspaceHomeUiState(
    val greeting: String,
    val lastSyncLabel: String,
    val selectedFilter: WorkspaceFilter,
    val filters: List<WorkspaceFilter>,
    val items: List<WorkspaceItem>
)

enum class WorkspaceFilter {
    All,
    Notes,
    Tasks,
    Ideas,
    Files
}