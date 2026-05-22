package com.paulmathew.pulsesync.ui.conflict

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import com.paulmathew.pulsesync.ui.components.OperationalPanel
import com.paulmathew.pulsesync.ui.theme.FailureRed
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurface
import com.paulmathew.pulsesync.ui.theme.PanelSurfaceElevated

@Composable
fun ConflictResolutionRoute(
    viewModel: ConflictViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ConflictResolutionScreen(
        state = state,
        onConflictSelected = viewModel::onConflictSelected,
        onStrategySelected = viewModel::onStrategySelected,
        onResolveSelectedConflict = viewModel::onResolveSelectedConflict,
        onSelectionCleared = viewModel::onSelectionCleared
    )
}

@Composable
fun ConflictResolutionScreen(
    state: ConflictUiState,
    onConflictSelected: (String) -> Unit,
    onStrategySelected: (ConflictResolutionStrategyUi) -> Unit,
    onResolveSelectedConflict: () -> Unit,
    onSelectionCleared: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GraphiteBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.conflicts.isEmpty()) {
            EmptyConflictState()
        } else {
            ConflictList(
                conflicts = state.conflicts,
                selectedConflictId = state.selectedConflict?.operationId,
                onConflictSelected = onConflictSelected
            )

            state.selectedConflict?.let { selectedConflict ->
                ConflictDetailPanel(
                    conflict = selectedConflict,
                    onStrategySelected = onStrategySelected,
                    onResolveSelectedConflict = onResolveSelectedConflict,
                    onSelectionCleared = onSelectionCleared
                )
            }
        }
    }
}

@Composable
private fun ConflictHeader() {
    Column {
        Text(
            text = "Conflict Resolution",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Inspect divergent local and remote sync versions",
            color = TextSecondary
        )
    }
}

@Composable
private fun EmptyConflictState() {
    OperationalPanel {
        Text(
            text = "No active conflicts",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Divergent local and remote versions will appear here when detected",
            color = TextSecondary
        )
    }
}

@Composable
private fun ConflictList(
    conflicts: List<ConflictItemUiState>,
    selectedConflictId: String?,
    onConflictSelected: (String) -> Unit
) {
    OperationalPanel {
        Text(
            text = "Active Conflicts",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            conflicts.forEach { conflict ->
                ConflictListItem(
                    conflict = conflict,
                    selected = conflict.operationId == selectedConflictId,
                    onConflictSelected = onConflictSelected
                )
            }
        }
    }
}

@Composable
private fun ConflictListItem(
    conflict: ConflictItemUiState,
    selected: Boolean,
    onConflictSelected: (String) -> Unit
) {
    val borderColor = if (selected) OperationalGreen else PanelBorder

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onConflictSelected(conflict.operationId) }
            .background(
                color = PanelSurfaceElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = conflict.resourcePath,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = conflict.detectedAtLabel,
                color = TextSecondary
            )
        }

        Text(
            text = "Local: ${conflict.localSummary}",
            color = TextSecondary
        )

        Text(
            text = "Remote: ${conflict.remoteSummary}",
            color = TextSecondary
        )
    }
}

@Composable
private fun ConflictDetailPanel(
    conflict: ConflictDetailUiState,
    onStrategySelected: (ConflictResolutionStrategyUi) -> Unit,
    onResolveSelectedConflict: () -> Unit,
    onSelectionCleared: () -> Unit
) {
    OperationalPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Conflict in ${conflict.resourcePath}",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            TextButton(onClick = onSelectionCleared) {
                Text(
                    text = "Close",
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VersionPanel(
                title = "Local Version",
                version = conflict.localVersion,
                modifier = Modifier.weight(1f)
            )

            VersionPanel(
                title = "Remote Version",
                version = conflict.remoteVersion,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Resolution Strategy",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        StrategySelector(
            selectedStrategy = conflict.selectedStrategy,
            onStrategySelected = onStrategySelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onResolveSelectedConflict,
            colors = ButtonDefaults.buttonColors(
                containerColor = OperationalGreen,
                contentColor = GraphiteBackground
            )
        ) {
            Text(
                text = "Resolve Conflict",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
@Composable
private fun VersionPanel(
    title: String,
    version: ConflictVersionUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = PanelSurfaceElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                border = BorderStroke(1.dp, PanelBorder),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            color = OperationalGreen,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = version.summary,
            color = TextPrimary
        )

        Text(
            text = "Updated ${version.updatedAtLabel}",
            color = TextSecondary
        )

        Text(
            text = version.payloadHash,
            color = TextSecondary
        )
    }
}
@Composable
private fun StrategySelector(
    selectedStrategy: ConflictResolutionStrategyUi,
    onStrategySelected: (ConflictResolutionStrategyUi) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ConflictResolutionStrategyUi.entries.forEach { strategy ->
            FilterChip(
                selected = strategy == selectedStrategy,
                onClick = { onStrategySelected(strategy) },
                label = {
                    Text(text = strategy.label)
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = PanelSurface,
                    labelColor = TextSecondary,
                    selectedContainerColor = OperationalGreen,
                    selectedLabelColor = GraphiteBackground
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = strategy == selectedStrategy,
                    borderColor = PanelBorder,
                    selectedBorderColor = OperationalGreen
                )
            )
        }
    }
}

private val ConflictResolutionStrategyUi.label: String
    get() = when (this) {
        ConflictResolutionStrategyUi.LocalWins -> "Local Wins"
        ConflictResolutionStrategyUi.RemoteWins -> "Remote Wins"
        ConflictResolutionStrategyUi.Merge -> "Merge"
        ConflictResolutionStrategyUi.ManualReview -> "Manual Review"
    }

@Preview(
    name = "Conflict Resolution - Empty",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun ConflictResolutionEmptyPreview() {
    PulseSyncTheme {
        ConflictResolutionScreen(
            state = ConflictPreviewData.emptyState,
            onConflictSelected = {},
            onStrategySelected = {},
            onResolveSelectedConflict = {},
            onSelectionCleared = {}
        )
    }
}
@Preview(
    name = "Conflict Resolution - Selected",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun ConflictResolutionSelectedPreview() {
    PulseSyncTheme {
        ConflictResolutionScreen(
            state = ConflictPreviewData.selectedConflictState,
            onConflictSelected = {},
            onStrategySelected = {},
            onResolveSelectedConflict = {},
            onSelectionCleared = {}
        )
    }
}