package com.paulmathew.pulsesync.ui.conflict.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.model.conflict.ConflictResolutionChoice
import com.paulmathew.pulsesync.model.conflict.ConflictVersion
import com.paulmathew.pulsesync.ui.components.PulseSurface
import com.paulmathew.pulsesync.ui.components.PulseSurfaceTone
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConflictResolutionSheet(
    state: ConflictResolutionUiState,
    onChoiceSelected: (ConflictResolutionChoice) -> Unit,
    onResolveClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = PulseColors.BackgroundPrimary,
        contentColor = PulseColors.TextPrimary,
        modifier = modifier
    ) {
        ConflictResolutionContent(
            state = state,
            onChoiceSelected = onChoiceSelected,
            onResolveClick = onResolveClick
        )
    }
}

@Composable
private fun ConflictResolutionContent(
    state: ConflictResolutionUiState,
    onChoiceSelected: (ConflictResolutionChoice) -> Unit,
    onResolveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PulseThemeTokens.spacing.lg)
            .padding(bottom = PulseThemeTokens.spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.lg)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.xs)) {
            Text(
                text = "Resolve Conflict",
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${state.conflict.documentTitle} · ${state.conflict.subtitle}",
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        ConflictAttentionBanner()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
        ) {
            ConflictVersionCard(
                version = state.conflict.yourVersion,
                accentColor = PulseColors.AccentPrimary,
                modifier = Modifier.weight(1f)
            )

            ConflictVersionCard(
                version = state.conflict.remoteVersion,
                accentColor = PulseColors.TrustGreen,
                modifier = Modifier.weight(1f)
            )
        }

        MergePreviewSection(
            preview = state.conflict.mergePreview
        )

        Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)) {
            ResolutionButton(
                text = "Keep Mine",
                selected = state.selectedChoice == ConflictResolutionChoice.KeepMine,
                onClick = { onChoiceSelected(ConflictResolutionChoice.KeepMine) }
            )

            ResolutionButton(
                text = "Use Theirs",
                selected = state.selectedChoice == ConflictResolutionChoice.UseTheirs,
                onClick = { onChoiceSelected(ConflictResolutionChoice.UseTheirs) }
            )

            ResolutionButton(
                text = "Merge",
                selected = state.selectedChoice == ConflictResolutionChoice.Merge,
                onClick = { onChoiceSelected(ConflictResolutionChoice.Merge) }
            )
        }

        Button(
            onClick = onResolveClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PulseColors.AccentPrimary,
                contentColor = PulseColors.TextPrimary
            )
        ) {
            Text(text = "Resolve Conflict")
        }
    }
}

@Composable
fun ConflictAttentionBanner(
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier.fillMaxWidth(),
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = PulseColors.AccentPrimary.copy(alpha = 0.35f),
        contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
    ) {
        Text(
            text = "PulseSync found two valid edits. Nothing was lost.",
            color = PulseColors.TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ConflictVersionCard(
    version: ConflictVersion,
    accentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    PulseSurface(
        modifier = modifier,
        tone = PulseSurfaceTone.Base,
        shape = PulseThemeTokens.radii.large,
        borderColor = accentColor.copy(alpha = 0.45f),
        contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)) {
            Text(
                text = version.label,
                color = accentColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${version.authorLabel} · ${version.updatedAtLabel}",
                color = PulseColors.TextTertiary,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = version.content,
                color = PulseColors.TextPrimary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun MergePreviewSection(
    preview: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.sm)
    ) {
        Text(
            text = "Merge Preview",
            color = PulseColors.TextPrimary,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        PulseSurface(
            tone = PulseSurfaceTone.Base,
            shape = PulseThemeTokens.radii.large,
            borderColor = PulseColors.BorderSubtle,
            contentPadding = PaddingValues(PulseThemeTokens.spacing.md)
        ) {
            Text(
                text = preview,
                color = PulseColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ResolutionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) {
                PulseColors.AccentPrimary.copy(alpha = 0.18f)
            } else {
                PulseColors.SurfacePrimary
            },
            contentColor = if (selected) PulseColors.AccentPrimary else PulseColors.TextSecondary
        )
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun ConflictResolutionContentPreview() {
    PulseTheme {
        ConflictResolutionContent(
            state = ConflictResolutionPreviewData.default,
            onChoiceSelected = {},
            onResolveClick = {}
        )
    }
}