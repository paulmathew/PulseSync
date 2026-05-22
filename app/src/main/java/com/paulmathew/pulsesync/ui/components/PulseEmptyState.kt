package com.paulmathew.pulsesync.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun PulseEmptyState(
    title: String,
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = PulseThemeTokens.motion.standardFade)
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
        ) {
            EmptyStateVisual(icon = icon)

            Text(
                text = title,
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = message,
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            if (actionLabel != null && onActionClick != null) {
                Button(
                    onClick = onActionClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PulseColors.AccentPrimary,
                        contentColor = PulseColors.TextPrimary
                    ),
                    contentPadding = PaddingValues(
                        horizontal = PulseThemeTokens.spacing.lg,
                        vertical = PulseThemeTokens.spacing.sm
                    )
                ) {
                    Text(text = actionLabel)
                }
            }
        }
    }
}

@Composable
private fun EmptyStateVisual(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(72.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PulseColors.AccentPrimary.copy(alpha = 0.22f),
                        PulseColors.SurfacePrimary.copy(alpha = 0.4f)
                    )
                ),
                shape = PulseThemeTokens.radii.full
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PulseColors.AccentPrimary,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun NoWorkspaceEmptyState(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PulseEmptyState(
        title = "No workspaces yet",
        message = "Create your first offline-first workspace and keep working from anywhere.",
        icon = Icons.Outlined.Workspaces,
        actionLabel = "Create Workspace",
        onActionClick = onCreateClick,
        modifier = modifier
    )
}

@Composable
fun NoActivityEmptyState(
    modifier: Modifier = Modifier
) {
    PulseEmptyState(
        title = "No activity yet",
        message = "Edits, sync updates, and collaboration moments will appear here.",
        icon = Icons.Outlined.CloudDone,
        modifier = modifier
    )
}

@Composable
fun NoSharedItemsEmptyState(
    modifier: Modifier = Modifier
) {
    PulseEmptyState(
        title = "Nothing shared yet",
        message = "Shared spaces will appear here when collaboration begins.",
        icon = Icons.Outlined.Group,
        modifier = modifier
    )
}

@Composable
fun SyncCompletedEmptyState(
    modifier: Modifier = Modifier
) {
    PulseEmptyState(
        title = "You’re fully synced.",
        message = "Everything is ready when you are.",
        icon = Icons.Outlined.CheckCircle,
        modifier = modifier
    )
}

@Composable
fun NoSearchResultsEmptyState(
    query: String,
    modifier: Modifier = Modifier
) {
    PulseEmptyState(
        title = "No results found",
        message = "Nothing matched “$query”. Try a different search.",
        icon = Icons.Outlined.SearchOff,
        modifier = modifier
    )
}

@Preview
@Composable
private fun PulseEmptyStatePreview() {
    PulseTheme {
        NoWorkspaceEmptyState(onCreateClick = {})
    }
}