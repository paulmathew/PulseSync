package com.paulmathew.pulsesync.ui.sync

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.model.sync.SyncStatus
import com.paulmathew.pulsesync.sync.rememberSyncStatusVisuals
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun SyncStateIndicator(
    status: SyncStatus,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val visuals = rememberSyncStatusVisuals(status)

    AnimatedContent(
        targetState = status,
        transitionSpec = {
            fadeIn(tween(180)) togetherWith fadeOut(tween(180)) using SizeTransform(clip = false)
        },
        label = "SyncStateIndicator",
        modifier = modifier
    ) { targetStatus ->
        val targetVisuals = rememberSyncStatusVisuals(targetStatus)

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SyncPulseDot(
                status = targetStatus,
                color = targetVisuals.color
            )

            if (showLabel) {
                Text(
                    text = targetVisuals.label,
                    color = targetVisuals.color,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SyncConfidenceBadge(
    status: SyncStatus,
    modifier: Modifier = Modifier
) {
    val visuals = rememberSyncStatusVisuals(status)

    Row(
        modifier = modifier
            .background(
                color = visuals.color.copy(alpha = 0.12f),
                shape = PulseThemeTokens.radii.full
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SyncPulseDot(
            status = status,
            color = visuals.color
        )

        Text(
            text = visuals.label,
            color = visuals.color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PendingChangesSummary(
    pendingChanges: Int,
    modifier: Modifier = Modifier
) {
    if (pendingChanges <= 0) return

    Text(
        text = "$pendingChanges local changes waiting",
        color = PulseColors.WarningAmber,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Medium,
        modifier = modifier
    )
}

@Composable
fun OfflineBadge(
    pendingChanges: Int,
    modifier: Modifier = Modifier
) {
    SyncConfidenceBadge(
        status = SyncStatus.OfflinePending(pendingChanges),
        modifier = modifier
    )
}

@Composable
fun RetryStatusRow(
    attemptLabel: String,
    modifier: Modifier = Modifier
) {
    val status = SyncStatus.Retrying(attemptLabel)
    val visuals = rememberSyncStatusVisuals(status)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        SyncStateIndicator(status = status)

        Text(
            text = visuals.description,
            color = PulseColors.TextTertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun SyncPulseDot(
    status: SyncStatus,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    val shouldPulse = status is SyncStatus.Syncing || status is SyncStatus.Retrying

    val alpha by if (shouldPulse) {
        rememberInfiniteTransition(label = "SyncPulse").animateFloat(
            initialValue = 0.45f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900),
                repeatMode = RepeatMode.Reverse
            ),
            label = "SyncPulseAlpha"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(1f) }
    }

    Box(
        modifier = modifier
            .size(8.dp)
            .alpha(alpha)
            .background(
                color = color,
                shape = PulseThemeTokens.radii.full
            )
    )
}

@Preview
@Composable
private fun SyncStatusComponentsPreview() {
    PulseTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SyncConfidenceBadge(SyncStatus.Synced)
            SyncConfidenceBadge(SyncStatus.Syncing)
            SyncConfidenceBadge(SyncStatus.OfflinePending(3))
            SyncConfidenceBadge(SyncStatus.Retrying("Retrying in 8s"))
            SyncConfidenceBadge(SyncStatus.NeedsAttention)
        }
    }
}