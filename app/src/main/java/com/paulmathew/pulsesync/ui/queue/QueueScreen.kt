package com.paulmathew.pulsesync.ui.queue

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paulmathew.pulsesync.model.QueuedOperation
import com.paulmathew.pulsesync.model.label
import com.paulmathew.pulsesync.ui.components.OperationalPanel
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurface
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paulmathew.pulsesync.model.QueuedOperationStatus
import com.paulmathew.pulsesync.ui.components.StatusBadge
import com.paulmathew.pulsesync.ui.components.StatusTone
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurfaceElevated

@Composable
fun QueueRoute(
    viewModel: QueueViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    QueueScreen(
        state = state,
        onFilterSelected = viewModel::onFilterSelected,
        onOperationSelected = viewModel::onOperationSelected
    )
}

@Composable
fun QueueScreen(
    state: QueueUiState,
    onFilterSelected: (QueueFilter) -> Unit,
    onOperationSelected: (QueuedOperation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GraphiteBackground)
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QueueHeader()

        QueueFilterStrip(
            filters = state.filters,
            selectedFilter = state.selectedFilter,
            onFilterSelected = onFilterSelected
        )

        QueueOperationList(
            operations = state.operations,
            onOperationSelected = onOperationSelected
        )
    }
}

@Composable
private fun QueueHeader() {
    Column {
        Text(
            text = "Queue",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Pending, syncing, and failed operations",
            color = TextSecondary
        )
    }
}

@Composable
private fun QueueFilterStrip(
    filters: List<QueueFilter>,
    selectedFilter: QueueFilter,
    onFilterSelected: (QueueFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = {
                    onFilterSelected(filter)
                },
                label = {
                    Text(text = filter.label)
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = PanelSurface,
                    labelColor = TextSecondary,
                    selectedContainerColor = OperationalGreen,
                    selectedLabelColor = GraphiteBackground
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = filter == selectedFilter,
                    borderColor = PanelBorder,
                    selectedBorderColor = OperationalGreen
                )
            )
        }
    }
}

@Composable
private fun QueueOperationList(
    operations: List<QueuedOperation>,
    onOperationSelected: (QueuedOperation) -> Unit
) {
    OperationalPanel {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            operations.forEach { operation ->
                QueueOperationRow(
                    operation = operation,
                    onOperationSelected = onOperationSelected
                )
            }
        }
    }
}

@Composable
private fun QueueOperationRow(
    operation: QueuedOperation,
    onOperationSelected: (QueuedOperation) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOperationSelected(operation) }
            .background(
                color = PanelSurfaceElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                border = BorderStroke(1.dp, PanelBorder),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${operation.method.label} ${operation.resourcePath}",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Queued ${operation.enqueuedAt}",
                color = TextSecondary
            )

            operation.nextRetryLabel?.let { retryLabel ->
                Text(
                    text = retryLabel,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        StatusBadge(
            label = operation.status.label,
            tone = operation.status.toStatusTone()
        )
    }
}

private val QueuedOperationStatus.label: String
    get() = when (this) {
        QueuedOperationStatus.Pending -> "PENDING"
        QueuedOperationStatus.Syncing -> "SYNCING"
        QueuedOperationStatus.Failed -> "FAILED"
    }

private fun QueuedOperationStatus.toStatusTone(): StatusTone {
    return when (this) {
        QueuedOperationStatus.Pending -> StatusTone.Warning
        QueuedOperationStatus.Syncing -> StatusTone.Info
        QueuedOperationStatus.Failed -> StatusTone.Error
    }
}

@Preview(
    name = "Queue",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun QueueScreenPreview() {
    PulseSyncTheme {
        QueueScreen(
            state = QueuePreviewData.defaultState,
            onFilterSelected = {},
            onOperationSelected = {}
        )
    }
}