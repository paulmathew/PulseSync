package com.paulmathew.pulsesync.ui.profile

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
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.ui.components.PulsePressable
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun ProfileRoute(
    onDeveloperDiagnosticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileScreen(
        onDeveloperDiagnosticsClick = onDeveloperDiagnosticsClick,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    onDeveloperDiagnosticsClick: () -> Unit,
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
                text = "Profile",
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sync settings, workspace identity, and internal diagnostics.",
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        PulseSurface(
            tone = PulseSurfaceTone.Base,
            shape = PulseThemeTokens.radii.large,
            borderColor = PulseColors.BorderSubtle,
            contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = PulseColors.AccentPrimary
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Paul Mathew",
                        color = PulseColors.TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Local-first workspace owner",
                        color = PulseColors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        ProfileActionRow(
            title = "Sync Health",
            subtitle = "All workspace changes are protected locally.",
            icon = Icons.Outlined.CloudDone,
            onClick = {}
        )

        ProfileActionRow(
            title = "Developer Diagnostics",
            subtitle = "Internal tools for inspecting synchronization behavior.",
            icon = Icons.Outlined.BugReport,
            onClick = onDeveloperDiagnosticsClick
        )
    }
}

@Composable
private fun ProfileActionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    PulsePressable(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        PulseSurface(
            modifier = Modifier
                .fillMaxWidth(),
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
                    tint = PulseColors.TextSecondary
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
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    PulseTheme {
        ProfileScreen(
            onDeveloperDiagnosticsClick = {}
        )
    }
}