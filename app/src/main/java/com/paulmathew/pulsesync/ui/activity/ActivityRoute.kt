package com.paulmathew.pulsesync.ui.activity

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.OfflineBolt
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.model.activity.ActivityEvent
import com.paulmathew.pulsesync.model.activity.ActivityEventType
import com.paulmathew.pulsesync.model.activity.ActivityFeedUiState
import com.paulmathew.pulsesync.ui.components.PulsePressable
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.sync.SyncStateIndicator
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun ActivityRoute(
    modifier: Modifier = Modifier
) {
    ActivityScreen(
        state = ActivityPreviewData.default,
        modifier = modifier
    )
}

@Composable
fun ActivityScreen(
    state: ActivityFeedUiState,
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
            Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)) {
                Text(
                    text = "Activity",
                    color = PulseColors.TextPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Everything that matters across your workspace.",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (state.isEmpty) {
            item {
                ActivityEmptyState()
            }
        } else {
            activitySection("Today", state.today)
            activitySection("Yesterday", state.yesterday)
            activitySection("Earlier", state.earlier)
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.activitySection(
    title: String,
    events: List<ActivityEvent>
) {
    if (events.isEmpty()) return

    item {
        ActivityTimelineSection(title = title)
    }

    items(
        items = events,
        key = { event -> event.id }
    ) { event ->
        ActivityEventRow(
            event = event,
            modifier = Modifier.animateItem()
        )
    }
}

@Composable
fun ActivityTimelineSection(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title.uppercase(),
        color = PulseColors.TextTertiary,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
fun ActivityEventRow(
    event: ActivityEvent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
    ) {
        ActivityEventIcon(type = event.type)
        PulsePressable(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            PulseSurface(
                modifier = Modifier.weight(1f),
                tone = PulseSurfaceTone.Base,
                shape = PulseThemeTokens.radii.large,
                borderColor = PulseColors.BorderSubtle.copy(alpha = 0.55f),
                contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)) {
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
                                text = event.title,
                                color = PulseColors.TextPrimary,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = event.subtitle,
                                color = PulseColors.TextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        event.syncStatus?.let { status ->
                            SyncStateIndicator(
                                status = status,
                                showLabel = false
                            )
                        }
                    }

                    Text(
                        text = event.timestampLabel,
                        color = PulseColors.TextTertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityEventIcon(
    type: ActivityEventType,
    modifier: Modifier = Modifier
) {
    val icon = activityIcon(type)
    val color = activityColor(type)

    Box(
        modifier = modifier
            .size(34.dp)
            .background(
                color = color.copy(alpha = 0.14f),
                shape = PulseThemeTokens.radii.full
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun ActivityEmptyState(
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier.fillMaxWidth(),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.BorderSubtle,
        contentPadding = PaddingValues(PulseThemeTokens.spacing.lg)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)) {
            Text(
                text = "No activity yet",
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Workspace edits, sync updates, and collaboration moments will appear here.",
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun activityIcon(type: ActivityEventType): ImageVector {
    return when (type) {
        ActivityEventType.Edit -> Icons.Outlined.Edit
        ActivityEventType.Create -> Icons.Outlined.CheckCircle
        ActivityEventType.Sync -> Icons.Outlined.Sync
        ActivityEventType.Offline -> Icons.Outlined.OfflineBolt
        ActivityEventType.Conflict -> Icons.Outlined.WarningAmber
        ActivityEventType.Member -> Icons.Outlined.Group
    }
}

private fun activityColor(type: ActivityEventType): Color {
    return when (type) {
        ActivityEventType.Edit -> PulseColors.AccentPrimary
        ActivityEventType.Create -> PulseColors.TrustGreen
        ActivityEventType.Sync -> PulseColors.TrustGreen
        ActivityEventType.Offline -> PulseColors.WarningAmber
        ActivityEventType.Conflict -> PulseColors.AccentPrimary
        ActivityEventType.Member -> PulseColors.TextSecondary
    }
}

@Preview
@Composable
private fun ActivityScreenPreview() {
    PulseTheme {
        ActivityScreen(state = ActivityPreviewData.default)
    }
}