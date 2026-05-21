package com.paulmathew.pulsesync.ui.editor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.paulmathew.pulsesync.model.editor.EditorIntent
import com.paulmathew.pulsesync.model.editor.EditorSyncState
import com.paulmathew.pulsesync.model.editor.EditorUiState
import com.paulmathew.pulsesync.ui.editor.EditorPreviewData.designSystem
import com.paulmathew.pulsesync.ui.editor.EditorPreviewData.meetingNotes
import com.paulmathew.pulsesync.ui.editor.EditorPreviewData.projectAurora
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens

@Composable
fun EditorRoute(
    documentId: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initialState = EditorPreviewData.documentFor(documentId)

    val scope = rememberCoroutineScope()
    val holder = remember(documentId) {
        EditorStateHolder(
            initialState = initialState,
            scope = scope
        )
    }

    EditorScreen(
        state = holder.state,
        onIntent = holder::dispatch,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun EditorScreen(
    state: EditorUiState,
    onIntent: (EditorIntent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .padding(horizontal = PulseThemeTokens.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.lg)
    ) {
        EditorTopBar(
            syncState = state.syncState,
            updatedAtLabel = state.updatedAtLabel,
            onBackClick = onBackClick
        )

        BasicTextField(
            value = state.title,
            onValueChange = { onIntent(EditorIntent.TitleChanged(it)) },
            textStyle = MaterialTheme.typography.displaySmall.copy(
                color = PulseColors.TextPrimary,
                fontWeight = FontWeight.Bold
            ),
            cursorBrush = SolidColor(PulseColors.AccentPrimary),
            modifier = Modifier.padding(top = PulseThemeTokens.spacing.md),
            decorationBox = { innerTextField ->
                if (state.title.isBlank()) {
                    Text(
                        text = "Untitled",
                        style = MaterialTheme.typography.displaySmall,
                        color = PulseColors.TextTertiary
                    )
                }
                innerTextField()
            }
        )

        BasicTextField(
            value = state.body,
            onValueChange = { onIntent(EditorIntent.BodyChanged(it)) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = PulseColors.TextSecondary,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.25
            ),
            cursorBrush = SolidColor(PulseColors.AccentPrimary),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (state.body.isBlank()) {
                    Text(
                        text = "Start writing...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = PulseColors.TextTertiary
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun EditorTopBar(
    syncState: EditorSyncState,
    updatedAtLabel: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = PulseColors.TextPrimary
            )
        }

        AnimatedContent(
            targetState = syncState,
            transitionSpec = {
                fadeIn() togetherWith fadeOut() using SizeTransform(clip = false)
            },
            label = "EditorSyncState"
        ) { targetState ->
            Text(
                text = syncLabel(targetState, updatedAtLabel),
                style = MaterialTheme.typography.labelMedium,
                color = syncColor(targetState)
            )
        }
    }
}

private fun syncLabel(
    syncState: EditorSyncState,
    updatedAtLabel: String
): String {
    return when (syncState) {
        EditorSyncState.Synced -> updatedAtLabel
        EditorSyncState.Syncing -> "Saving..."
        EditorSyncState.OfflinePending -> "Saved offline"
        EditorSyncState.Retrying -> "Retrying..."
    }
}

private fun syncColor(syncState: EditorSyncState) = when (syncState) {
    EditorSyncState.Synced -> PulseColors.TrustGreen
    EditorSyncState.Syncing -> PulseColors.AccentPrimary
    EditorSyncState.OfflinePending -> PulseColors.WarningAmber
    EditorSyncState.Retrying -> PulseColors.WarningAmber
}

@Preview
@Composable
private fun EditorScreenPreview() {
    PulseTheme {
        EditorScreen(
            state = EditorPreviewData.projectAurora,
            onIntent = {},
            onBackClick = {}
        )
    }
}
