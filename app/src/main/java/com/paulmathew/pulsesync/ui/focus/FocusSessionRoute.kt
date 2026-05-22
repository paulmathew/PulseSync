package com.paulmathew.pulsesync.ui.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.model.focus.FocusActivity
import com.paulmathew.pulsesync.model.focus.FocusItem
import com.paulmathew.pulsesync.model.focus.FocusItemType
import com.paulmathew.pulsesync.model.focus.FocusSessionUiState
import com.paulmathew.pulsesync.model.sync.SyncStatus
import com.paulmathew.pulsesync.model.workspace.CollaboratorTone
import com.paulmathew.pulsesync.ui.components.PulsePressable
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.home.CollaboratorAvatarStack
import com.paulmathew.pulsesync.ui.sync.SyncConfidenceBadge
import com.paulmathew.pulsesync.ui.sync.SyncStateIndicator
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun FocusSessionRoute(
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(FocusSessionPreviewData.default) }

    FocusSessionScreen(
        state = state,
        onDraftChanged = { value ->
            state = state.copy(draftText = value)
        },
        onAddItem = {
            val draft = state.draftText.trim()
            if (draft.isNotEmpty()) {
                val newItem = FocusItem(
                    id = "local-${state.session.items.size + 1}",
                    title = draft,
                    detail = "Saved locally and syncing quietly.",
                    type = FocusItemType.Task,
                    syncStatus = SyncStatus.Syncing
                )

                state = state.copy(
                    draftText = "",
                    session = state.session.copy(
                        items = listOf(newItem) + state.session.items,
                        activity = listOf(
                            FocusActivity(
                                id = "activity-${state.session.activity.size + 1}",
                                message = "You added “$draft”",
                                timestampLabel = "Just now"
                            )
                        ) + state.session.activity,
                        syncStatus = SyncStatus.Syncing
                    )
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun FocusSessionScreen(
    state: FocusSessionUiState,
    onDraftChanged: (String) -> Unit,
    onAddItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary),
        contentPadding = PaddingValues(
            start = PulseThemeTokens.spacing.lg,
            end = PulseThemeTokens.spacing.lg,
            top = PulseThemeTokens.spacing.xl,
            bottom = PulseThemeTokens.spacing.xxl
        ),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.lg)
    ) {
        item {
            FocusSessionHeader(state = state)
        }

        item {
            FocusDraftComposer(
                value = state.draftText,
                onValueChange = onDraftChanged,
                onAddClick = onAddItem
            )
        }

        item {
            Text(
                text = "Shared space",
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(
            items = state.session.items,
            key = { item -> item.id }
        ) { item ->
            FocusItemCard(item = item)
        }

        item {
            FocusActivityStrip(activity = state.session.activity)
        }
    }
}
@Composable
private fun FocusSessionHeader(
    state: FocusSessionUiState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.session.title,
                    color = PulseColors.TextPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = state.session.subtitle,
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            SyncConfidenceBadge(status = state.session.syncStatus)
        }

        CollaboratorAvatarStack(
            collaborators = state.session.collaborators.map {
                com.paulmathew.pulsesync.model.workspace.Collaborator(
                    id = it.id,
                    displayName = it.name,
                    initials = it.initials,
                    avatarTone = CollaboratorTone.Blue
                )
            }
        )
    }
}

@Composable
private fun FocusDraftComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    PulseSurface(
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.BorderSubtle,
        contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = PulseColors.TextPrimary
                ),
                cursorBrush = SolidColor(PulseColors.AccentPrimary),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = "Add a focus task or note...",
                            color = PulseColors.TextTertiary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }
            )

            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add",
                    tint = PulseColors.AccentPrimary
                )
            }
        }
    }
}

@Composable
private fun FocusItemCard(
    item: FocusItem
) {
    PulsePressable(
        onClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        PulseSurface(
            modifier = Modifier.fillMaxWidth(),
            tone = PulseSurfaceTone.Base,
            shape = PulseThemeTokens.radii.large,
            borderColor = PulseColors.BorderSubtle.copy(alpha = 0.55f),
            contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = if (item.type == FocusItemType.Task) {
                        Icons.Outlined.CheckCircle
                    } else {
                        Icons.Outlined.Description
                    },
                    contentDescription = null,
                    tint = PulseColors.AccentPrimary
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)
                ) {
                    Text(
                        text = item.title,
                        color = PulseColors.TextPrimary,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = item.detail,
                        color = PulseColors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )

                    SyncStateIndicator(
                        status = item.syncStatus,
                        showLabel = true
                    )
                }
            }
        }
    }
}

@Composable
private fun FocusActivityStrip(
    activity: List<FocusActivity>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)
    ) {
        Text(
            text = "Live activity",
            color = PulseColors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        activity.take(3).forEach { event ->
            Text(
                text = "${event.message} · ${event.timestampLabel}",
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun FocusSessionScreenPreview() {
    PulseTheme {
        FocusSessionScreen(
            state = FocusSessionPreviewData.default,
            onDraftChanged = {},
            onAddItem = {}
        )
    }
}