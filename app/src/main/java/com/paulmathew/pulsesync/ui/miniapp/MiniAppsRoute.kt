package com.paulmathew.pulsesync.ui.miniapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.model.miniapp.MiniAppDefinition
import com.paulmathew.pulsesync.model.miniapp.MiniAppType
import com.paulmathew.pulsesync.ui.components.PulsePressable
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment

@Composable
fun MiniAppsRoute(
    onMiniAppClick: (MiniAppDefinition) -> Unit,
    modifier: Modifier = Modifier
) {
    MiniAppsScreen(
        state = MiniAppsPreviewData.default,
        onMiniAppClick = onMiniAppClick,
        modifier = modifier
    )
}

@Composable
fun MiniAppsScreen(
    state: MiniAppsUiState,
    onMiniAppClick: (MiniAppDefinition) -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)
            ) {
                Text(
                    text = "Mini Apps",
                    color = PulseColors.TextPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Offline-first collaborative experiences powered by PulseSync.",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(
            items = state.apps,
            key = { app -> app.id }
        ) { app ->
            MiniAppRow(
                app = app,
                onClick = { onMiniAppClick(app) }
            )
        }
    }
}

@Composable
private fun MiniAppRow(
    app: MiniAppDefinition,
    onClick: () -> Unit
) {
    val enabled = app.type == MiniAppType.FocusSession

    PulsePressable(
        onClick = {
            if (enabled) onClick()
        },
        enabled = enabled,
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = miniAppIcon(app.type),
                    contentDescription = null,
                    tint = if (enabled) PulseColors.AccentPrimary else PulseColors.TextTertiary
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.title,
                        color = if (enabled) PulseColors.TextPrimary else PulseColors.TextSecondary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = app.subtitle,
                        color = PulseColors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = app.statusLabel,
                        color = if (enabled) PulseColors.TrustGreen else PulseColors.TextTertiary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = if (enabled) PulseColors.TextTertiary else PulseColors.BorderSubtle
                )
            }
        }
    }
}

private fun miniAppIcon(type: MiniAppType): ImageVector {
    return when (type) {
        MiniAppType.FocusSession -> Icons.Outlined.Timer
        MiniAppType.MeetingNotes -> Icons.Outlined.EditNote
        MiniAppType.SharedBrainstorm -> Icons.Outlined.Lightbulb
        MiniAppType.OfflineJournal -> Icons.Outlined.Book
    }
}

@Preview
@Composable
private fun MiniAppsScreenPreview() {
    PulseTheme {
        MiniAppsScreen(
            state = MiniAppsPreviewData.default,
            onMiniAppClick = {}
        )
    }
}