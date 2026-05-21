package com.paulmathew.pulsesync.ui.editor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.model.editor.EditorIntent
import com.paulmathew.pulsesync.model.editor.EditorSyncState
import com.paulmathew.pulsesync.model.editor.EditorUiState
import com.paulmathew.pulsesync.ui.theme.PulseColors
import com.paulmathew.pulsesync.ui.theme.PulseTheme
import com.paulmathew.pulsesync.ui.theme.PulseThemeTokens
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorScreen(
    state: EditorUiState,
    onIntent: (EditorIntent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isKeyboardVisible = WindowInsets.isImeVisible
    val editorScrollState = rememberScrollState()
    var isBodyFocused by remember { mutableStateOf(false) }
    var isFormattingExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.body, isBodyFocused) {
        if (isBodyFocused) {
            editorScrollState.animateScrollTo(editorScrollState.maxValue)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = PulseThemeTokens.spacing.lg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(editorScrollState),
            verticalArrangement = Arrangement.spacedBy(PulseThemeTokens.spacing.md)
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
                modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isBodyFocused = focusState.isFocused
                        if (!focusState.isFocused) {
                            isFormattingExpanded = false
                        }
                    },
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

        AnimatedVisibility(
            visible = isKeyboardVisible && isBodyFocused,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = PulseThemeTokens.spacing.xs,
                    bottom = PulseThemeTokens.spacing.xs
                ),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            EditorFormattingDock(
                expanded = isFormattingExpanded,
                onToggleExpanded = {
                    isFormattingExpanded = !isFormattingExpanded
                }
            )
        }
    }
}

@Composable
private fun EditorTopBar(
    syncState: EditorSyncState,
    updatedAtLabel: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
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
            EditorSyncPill(
                syncState = targetState,
                updatedAtLabel = updatedAtLabel
            )
        }
    }
}

@Composable
private fun EditorSyncPill(
    syncState: EditorSyncState,
    updatedAtLabel: String,
    modifier: Modifier = Modifier
) {
    val label = when (syncState) {
        EditorSyncState.Synced -> updatedAtLabel
        EditorSyncState.Syncing -> "Saving..."
        EditorSyncState.OfflinePending -> "Saved offline"
        EditorSyncState.Retrying -> "Retrying..."
    }

    val color = when (syncState) {
        EditorSyncState.Synced -> PulseColors.TrustGreen
        EditorSyncState.Syncing -> PulseColors.AccentPrimary
        EditorSyncState.OfflinePending -> PulseColors.WarningAmber
        EditorSyncState.Retrying -> PulseColors.WarningAmber
    }

    Row(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.12f),
                shape = PulseThemeTokens.radii.full
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(
                    color = color,
                    shape = PulseThemeTokens.radii.full
                )
        )

        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
private fun EditorFormattingDock(
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = PulseColors.SurfacePrimary.copy(alpha = 0.96f),
                shape = PulseThemeTokens.radii.full
            )
            .padding(horizontal = 12.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Aa",
            color = PulseColors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onToggleExpanded)
        )

        AnimatedVisibility(visible = expanded) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "≡",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "•",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "☑",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "▣",
                    color = PulseColors.TextSecondary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(name = "Editor - Synced")
@Composable
private fun EditorSyncedPreview() {
    PulseTheme {
        EditorScreen(
            state = EditorPreviewData.projectAurora,
            onIntent = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Editor - Offline Pending")
@Composable
private fun EditorOfflinePreview() {
    PulseTheme {
        EditorScreen(
            state = EditorPreviewData.projectAurora.copy(
                syncState = EditorSyncState.OfflinePending,
                updatedAtLabel = "Saved offline",
                hasLocalChanges = true
            ),
            onIntent = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Editor - Retrying")
@Composable
private fun EditorRetryingPreview() {
    PulseTheme {
        EditorScreen(
            state = EditorPreviewData.projectAurora.copy(
                syncState = EditorSyncState.Retrying,
                updatedAtLabel = "Retrying...",
                hasLocalChanges = true
            ),
            onIntent = {},
            onBackClick = {}
        )
    }
}
