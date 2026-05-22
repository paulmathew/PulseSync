package com.paulmathew.pulsesync.ui.diagnostics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CloudQueue
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.SettingsEthernet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun DiagnosticsHomeRoute(
    onNetworkClick: () -> Unit,
    onRuntimeEventsClick: () -> Unit,
    onObservabilityClick: () -> Unit,
    onQueueClick: () -> Unit,
    onConflictDebugClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DiagnosticsHomeScreen(
        onNetworkClick = onNetworkClick,
        onRuntimeEventsClick = onRuntimeEventsClick,
        onObservabilityClick = onObservabilityClick,
        onQueueClick = onQueueClick,
        onConflictDebugClick = onConflictDebugClick,
        modifier = modifier
    )
}

@Composable
fun DiagnosticsHomeScreen(
    onNetworkClick: () -> Unit,
    onRuntimeEventsClick: () -> Unit,
    onObservabilityClick: () -> Unit,
    onQueueClick: () -> Unit,
    onConflictDebugClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary)
            .padding(PulseThemeTokens.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.lg)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)) {
            Text(
                text = "Developer Diagnostics",
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Internal tools for inspecting synchronization behavior.",
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        DiagnosticsRow(
            title = "Network Simulation",
            subtitle = "Test offline, timeout, slow, and unreliable network behavior.",
            icon = Icons.Outlined.SettingsEthernet,
            onClick = onNetworkClick
        )

        DiagnosticsRow(
            title = "Runtime Events",
            subtitle = "Inspect low-level synchronization state transitions.",
            icon = Icons.Outlined.Route,
            onClick = onRuntimeEventsClick
        )

        DiagnosticsRow(
            title = "Observability Metrics",
            subtitle = "Review sync health, success rates, and retry behavior.",
            icon = Icons.Outlined.Analytics,
            onClick = onObservabilityClick
        )

        DiagnosticsRow(
            title = "Raw Operation Queue",
            subtitle = "View queued, pending, syncing, and failed operations.",
            icon = Icons.Outlined.CloudQueue,
            onClick = onQueueClick
        )

        DiagnosticsRow(
            title = "Conflict Debugging",
            subtitle = "Inspect divergent versions and resolution state.",
            icon = Icons.Outlined.BugReport,
            onClick = onConflictDebugClick
        )
    }
}

@Composable
private fun DiagnosticsRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    PulseSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.BorderSubtle,
        contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PulseColors.AccentPrimary
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = PulseColors.TextPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = subtitle,
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = PulseColors.TextTertiary
            )
        }
    }
}

@Preview
@Composable
private fun DiagnosticsHomeScreenPreview() {
    PulseTheme {
        DiagnosticsHomeScreen(
            onNetworkClick = {},
            onRuntimeEventsClick = {},
            onObservabilityClick = {},
            onQueueClick = {},
            onConflictDebugClick = {}
        )
    }
}