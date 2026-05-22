package com.paulmathew.pulsesync.ui.offline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.R
import com.paulmathew.pulsesync.model.offline.OfflineChange
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.diagnostics.DiagnosticsTopBar
import com.paulmathew.pulsesync.ui.sync.SyncStateIndicator
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun OfflineExperienceRoute(
    onViewQueueClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    OfflineExperienceScreen(
        state = OfflineExperiencePreviewData.default,
        onViewQueueClick = onViewQueueClick,
        modifier = modifier,
        onBackClick =onBackClick
    )
}

@Composable
fun OfflineExperienceScreen(
    state: OfflineExperienceUiState,
    onViewQueueClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {  Column(
    modifier = modifier
        .fillMaxSize()
        .background(PulseColors.BackgroundPrimary),
) {
    DiagnosticsTopBar(
        title = stringResource(id = R.string.app_name),
        description = "",
        onBackClick = onBackClick
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary)
            .padding(PulseThemeTokens.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OfflineReassurancePanel(
            title = state.title,
            message = state.message,
            pendingCount = state.pendingCount,
            onViewQueueClick = onViewQueueClick
        )

        OfflineChangesSection(
            changes = state.changes
        )
    }
}
}

@Composable
fun OfflineReassurancePanel(
    title: String,
    message: String,
    pendingCount: Int,
    onViewQueueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier.fillMaxWidth(),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.BorderSubtle,
        contentPadding = PaddingValues(PulseThemeTokens.spacing.lg)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.CloudOff,
                contentDescription = null,
                tint = PulseColors.TextSecondary
            )

            Text(
                text = title,
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = message,
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "$pendingCount changes saved locally",
                color = PulseColors.WarningAmber,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = onViewQueueClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PulseColors.AccentPrimary,
                    contentColor = PulseColors.TextPrimary
                )
            ) {
                Text(text = "View Offline Changes")
            }
        }
    }
}

@Composable
fun OfflineChangesSection(
    changes: List<OfflineChange>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
    ) {
        Text(
            text = "Continue working",
            color = PulseColors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        changes.forEach { change ->
            OfflineChangeItem(change = change)
        }
    }
}

@Composable
fun OfflineChangeItem(
    change: OfflineChange,
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier.fillMaxWidth(),
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
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = PulseColors.TextSecondary
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = change.title,
                    color = PulseColors.TextPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "${change.subtitle} · ${change.updatedAtLabel}",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                SyncStateIndicator(
                    status = change.status,
                    showLabel = true
                )
            }

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = PulseColors.TextTertiary
            )
        }
    }
}

@Preview
@Composable
private fun OfflineExperienceScreenPreview() {
    PulseTheme {
        OfflineExperienceScreen(
            state = OfflineExperiencePreviewData.default,
            onViewQueueClick = {},
            onBackClick = {}
        )
    }
}