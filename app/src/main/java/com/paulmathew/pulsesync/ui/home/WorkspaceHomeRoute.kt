package com.paulmathew.pulsesync.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.model.workspace.WorkspaceItem
import com.paulmathew.pulsesync.model.workspace.WorkspaceSyncState
import com.paulmathew.pulsesync.ui.components.PulseCard
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens
import com.paulmathew.pulsesync.model.sync.SyncStatus
import com.paulmathew.pulsesync.ui.sync.PendingChangesSummary
import com.paulmathew.pulsesync.ui.sync.SyncStateIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.paulmathew.pulsesync.ui.queue.v2.SyncQueueDrawer
import com.paulmathew.pulsesync.ui.queue.v2.SyncQueuePreviewData
@Composable
fun WorkspaceHomeRoute(
    onWorkspaceClick: (WorkspaceItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showQueueDrawer by remember { mutableStateOf(false) }

    WorkspaceHomeScreen(
        state = WorkspaceHomePreviewData.defaultState,
        onFilterSelected = {},
        onWorkspaceSelected = onWorkspaceClick,
        onCreateWorkspace = {},
        onQueueClick = {
            showQueueDrawer = true
        },
        modifier = modifier
    )

    if (showQueueDrawer) {
        SyncQueueDrawer(
            state = SyncQueuePreviewData.default,
            onDismissRequest = {
                showQueueDrawer = false
            }
        )
    }

}

@Composable
fun WorkspaceHomeScreen(
    state: WorkspaceHomeUiState,
    onFilterSelected: (WorkspaceFilter) -> Unit,
    onWorkspaceSelected: (WorkspaceItem) -> Unit,
    onCreateWorkspace: () -> Unit,
    onQueueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary)
            .padding(
                start = PulseThemeTokens.spacing.md,
                end = PulseThemeTokens.spacing.md,
                top = PulseThemeTokens.spacing.md
            )
    ) {
        WorkspaceTopBar(onQueueClick = onQueueClick)

        Spacer(modifier = Modifier.height(PulseThemeTokens.spacing.xl))

        WorkspaceHeader(
            title = state.greeting,
            lastSyncLabel = state.lastSyncLabel,
            onCreateWorkspace = onCreateWorkspace
        )

        Spacer(modifier = Modifier.height(PulseThemeTokens.spacing.lg))

        WorkspaceFilterRow(
            filters = state.filters,
            selectedFilter = state.selectedFilter,
            onFilterSelected = onFilterSelected
        )

        Spacer(modifier = Modifier.height(PulseThemeTokens.spacing.md))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(PulseColors.BackgroundPrimary),
            contentPadding = PaddingValues(
                start = PulseThemeTokens.spacing.xxs,
                end = PulseThemeTokens.spacing.xxs,
                top = PulseThemeTokens.spacing.xl,
                bottom = PulseThemeTokens.spacing.xxl
            ),
            verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
        ) {

            items(
                items = state.items,
                key = { item -> item.id }
            ) { item ->
                WorkspaceCard(
                    item = item,
                    onClick = { onWorkspaceSelected(item) }
                )
            }

            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun WorkspaceTopBar(onQueueClick: () -> Unit,) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "PulseSync",
            color = PulseColors.TextPrimary,
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint = PulseColors.TextSecondary
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = PulseColors.TextSecondary
                )
            }
            IconButton(onClick = onQueueClick) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = "Sync queue",
                    tint = PulseColors.TextSecondary
                )
            }
        }
    }
}

@Composable
private fun WorkspaceHeader(
    title: String,
    lastSyncLabel: String,
    onCreateWorkspace: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = PulseColors.TextPrimary,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = lastSyncLabel,
                    color = PulseColors.TextSecondary,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )

                SyncConfidenceDot(syncState = WorkspaceSyncState.Synced)
            }
        }

        IconButton(
            onClick = onCreateWorkspace,
            modifier = Modifier.background(
                color = PulseColors.AccentPrimary,
                shape = PulseThemeTokens.radii.full
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Create",
                tint = PulseColors.TextPrimary
            )
        }
    }
}

@Composable
private fun WorkspaceFilterRow(
    filters: List<WorkspaceFilter>,
    selectedFilter: WorkspaceFilter,
    onFilterSelected: (WorkspaceFilter) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(text = filter.label)
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = PulseColors.SurfacePrimary,
                    labelColor = PulseColors.TextSecondary,
                    selectedContainerColor = PulseColors.AccentPrimary,
                    selectedLabelColor = PulseColors.TextPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = filter == selectedFilter,
                    borderColor = PulseColors.BorderSubtle,
                    selectedBorderColor = PulseColors.AccentPrimary
                )
            )
        }
    }
}

@Composable
private fun WorkspaceCard(
    item: WorkspaceItem,
    onClick: () -> Unit
) {
    PulseSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.BorderSubtle.copy(alpha = 0.65f),
        tonalElevation = 2.dp,
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        color = PulseColors.TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = item.lastModifiedLabel,
                        color = PulseColors.TextTertiary,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                SyncConfidenceDot(syncState = item.syncState)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CollaboratorAvatarStack(
                    collaborators = item.collaborators
                )

                SyncStateIndicator(
                    status = item.syncState.toSyncStatus(item.pendingLocalChanges)
                )
            }

            Text(
                text = item.preview,
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (item.pendingLocalChanges > 0) {
                PendingChangesSummary(
                    pendingChanges = item.pendingLocalChanges
                )
            }
        }
    }
}

private val WorkspaceFilter.label: String
    get() = when (this) {
        WorkspaceFilter.All -> "All"
        WorkspaceFilter.Notes -> "Notes"
        WorkspaceFilter.Tasks -> "Tasks"
        WorkspaceFilter.Ideas -> "Ideas"
        WorkspaceFilter.Files -> "Files"
    }


@Preview(
    name = "Workspace Home",
    showBackground = true,
    backgroundColor = 0xFF070A10
)
@Composable
private fun WorkspaceHomeScreenPreview() {
    PulseTheme {
        WorkspaceHomeScreen(
            state = WorkspaceHomePreviewData.defaultState,
            onFilterSelected = {},
            onWorkspaceSelected = {},
            onCreateWorkspace = {},
            onQueueClick = {}
        )
    }
}
private fun WorkspaceSyncState.toSyncStatus(
    pendingChanges: Int
): SyncStatus {
    return when (this) {
        WorkspaceSyncState.Synced -> SyncStatus.Synced
        is WorkspaceSyncState.Syncing -> SyncStatus.Syncing
        is WorkspaceSyncState.Offline -> SyncStatus.OfflinePending(pendingChanges)
        is WorkspaceSyncState.NeedsAttention -> SyncStatus.NeedsAttention
    }
}