package com.paulmathew.pulsesync.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.model.workspace.Collaborator
import com.paulmathew.pulsesync.model.workspace.CollaboratorTone
import com.paulmathew.pulsesync.model.workspace.WorkspaceSyncState
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun CollaboratorAvatarStack(
    collaborators: List<Collaborator>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 3
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-6).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        collaborators.take(maxVisible).forEachIndexed { index, collaborator ->
            CollaboratorAvatar(
                collaborator = collaborator,
                modifier = Modifier.offset {
                    IntOffset(x = if (index == 0) 0 else -index * 10, y = 0)
                }
            )
        }

        val remaining = collaborators.size - maxVisible
        if (remaining > 0) {
            Text(
                text = "+$remaining",
                color = PulseColors.TextSecondary
            )
        }
    }
}

@Composable
private fun CollaboratorAvatar(
    collaborator: Collaborator,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(collaborator.avatarTone.color)
            .border(
                width = 1.dp,
                color = PulseColors.BackgroundPrimary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = collaborator.initials,
            color = PulseColors.TextPrimary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SyncConfidenceDot(
    syncState: WorkspaceSyncState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(syncState.color)
    )
}

private val CollaboratorTone.color: Color
    get() = when (this) {
        CollaboratorTone.Violet -> PulseColors.AccentPrimary
        CollaboratorTone.Green -> PulseColors.TrustGreen
        CollaboratorTone.Amber -> PulseColors.WarningAmber
        CollaboratorTone.Rose -> Color(0xFFE879A5)
        CollaboratorTone.Blue -> PulseColors.InfoBlue
    }

private val WorkspaceSyncState.color: Color
    get() = when (this) {
        WorkspaceSyncState.Synced -> PulseColors.TrustGreen
        is WorkspaceSyncState.Syncing -> PulseColors.AccentPrimary
        is WorkspaceSyncState.Offline -> PulseColors.WarningAmber
        is WorkspaceSyncState.NeedsAttention -> PulseColors.FailureRed
    }