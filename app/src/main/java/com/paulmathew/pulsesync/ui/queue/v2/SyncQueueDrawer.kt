package com.paulmathew.pulsesync.ui.queue.v2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.model.queue.SyncQueueItem
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.sync.SyncStateIndicator
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncQueueDrawer(
    state: SyncQueueDrawerUiState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = PulseColors.BackgroundPrimary,
        contentColor = PulseColors.TextPrimary,
        modifier = modifier
    ) {
        SyncQueueDrawerContent(
            state = state
        )
    }
}

@Composable
private fun SyncQueueDrawerContent(
    state: SyncQueueDrawerUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = PulseThemeTokens.spacing.sm,
            end = PulseThemeTokens.spacing.sm,
            bottom = PulseThemeTokens.spacing.xxl,
            top = PulseThemeTokens.spacing.lg
        ),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)
            ) {
                Text(
                    text = "Sync Queue",
                    color = PulseColors.TextPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Transparent local-first activity, handled quietly.",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        queueSection(
            title = "Uploading",
            items = state.uploading
        )

        queueSection(
            title = "Pending",
            items = state.pending
        )

        queueSection(
            title = "Retrying",
            items = state.retrying
        )

        queueSection(
            title = "Synced",
            items = state.synced
        )
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.queueSection(
    title: String,
    items: List<SyncQueueItem>
) {
    if (items.isEmpty()) return

    item {
        Text(
            text = title.uppercase(),
            color = PulseColors.TextTertiary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }

    items(
        items = items,
        key = { item -> item.id }
    ) { item ->
        SyncQueueRow(item = item)
    }
}

@Composable
private fun SyncQueueRow(
    item: SyncQueueItem,
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier.fillMaxWidth(),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.BorderSubtle.copy(alpha = 0.55f),
        contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
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
                        text = item.subtitle,
                        color = PulseColors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                SyncStateIndicator(
                    status = item.status,
                    showLabel = false
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)
            ) {
                Text(
                    text = item.timestampLabel,
                    color = PulseColors.TextTertiary,
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = item.payloadLabel,
                    color = PulseColors.TextTertiary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun SyncQueueDrawerContentPreview() {
    PulseTheme {
        SyncQueueDrawerContent(
            state = SyncQueuePreviewData.default
        )
    }
}