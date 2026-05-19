package com.paulmathew.pulsesync.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paulmathew.pulsesync.model.OperationStatus
import com.paulmathew.pulsesync.model.SyncHealthStatus
import com.paulmathew.pulsesync.ui.components.MetricTile
import com.paulmathew.pulsesync.ui.components.OperationalPanel
import com.paulmathew.pulsesync.ui.components.StatusBadge
import com.paulmathew.pulsesync.ui.components.StatusTone
import com.paulmathew.pulsesync.ui.components.TimelineEventRow
import com.paulmathew.pulsesync.ui.components.color
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.FailureRed
import com.paulmathew.pulsesync.ui.theme.WarningAmber

@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        state = state,
        onStartNextSync = viewModel::onStartNextSync,
        onCompleteActiveAsSuccess = viewModel::onCompleteActiveAsSuccess,
        onCompleteActiveAsTimeout = viewModel::onCompleteActiveAsTimeout,
        onApplyNetworkResult = viewModel::onCompleteActiveUsingNetworkProfile,
    )
}

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onStartNextSync: () -> Unit,
    onCompleteActiveAsSuccess: () -> Unit,
    onCompleteActiveAsTimeout: () -> Unit,
    onApplyNetworkResult: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GraphiteBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        DashboardTopBar()

        Text(
            text = "Dashboard",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        OverallStatusPanel(state = state)

        MetricStrip(state = state)

        DashboardRuntimeControls(
            onStartNextSync = onStartNextSync,
            onApplyNetworkResult = onApplyNetworkResult,
            onCompleteActiveAsTimeout = onCompleteActiveAsTimeout
        )

        RecentEventsPanel(state = state)
    }
}

@Composable
private fun DashboardTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "PulseSync",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        StatusBadge(
            label = "Online",
            tone = StatusTone.Success
        )
    }
}

@Composable
private fun OverallStatusPanel(
    state: DashboardUiState
) {
    OperationalPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Overall Status",
                    color = TextSecondary
                )

                Text(
                    text = state.healthLabel,
                    color = state.healthStatus.toStatusTone().color(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = state.healthDescription,
                    color = TextSecondary
                )
            }

            CircularProgressIndicator(
                progress = { state.syncProgressPercent / 100f },
                color = state.healthStatus.toStatusTone().color(),
                trackColor = TextSecondary.copy(alpha = 0.18f)
            )
        }
    }
}

@Composable
private fun MetricStrip(
    state: DashboardUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.metrics.forEach { metric ->
            MetricTile(
                label = metric.label,
                value = metric.value,
                tone = metric.status.toStatusTone(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RecentEventsPanel(
    state: DashboardUiState
) {
    OperationalPanel {
        Text(
            text = "Recent Events",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        state.recentEvents.forEach { event ->
            TimelineEventRow(event = event)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    PulseSyncTheme {
        DashboardScreen(
            state = DashboardPreviewData.healthyState,
            onStartNextSync = {},
            onCompleteActiveAsSuccess = {},
            onCompleteActiveAsTimeout = {},
            onApplyNetworkResult = {}

        )
    }
}

@Preview(
    name = "Dashboard - Healthy",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun DashboardHealthyPreview() {
    PulseSyncTheme {
        DashboardScreen(
            state = DashboardPreviewData.healthyState,
            onStartNextSync = {},
            onCompleteActiveAsSuccess = {},
            onCompleteActiveAsTimeout = {},
            onApplyNetworkResult = {}

        )
    }
}

@Composable
private fun DashboardRuntimeControls(
    onStartNextSync: () -> Unit,
    onApplyNetworkResult: () -> Unit,
    onCompleteActiveAsTimeout: () -> Unit
) {
    OperationalPanel {
        Text(
            text = "Runtime Controls",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onStartNextSync,
                colors = ButtonDefaults.buttonColors(
                    containerColor = OperationalGreen,
                    contentColor = GraphiteBackground
                )
            ) {
                Text(text = "Start")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = onApplyNetworkResult,
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningAmber,
                    contentColor = GraphiteBackground
                )
            ) {
                Text(text = "Apply Network")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = onCompleteActiveAsTimeout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = FailureRed,
                    contentColor = TextPrimary
                )
            ) {
                Text(text = "Timeout")
            }
        }
    }
}
@Preview(
    name = "Dashboard - Degraded",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun DashboardDegradedPreview() {
    PulseSyncTheme {
        DashboardScreen(
            state = DashboardPreviewData.healthyState,
            onStartNextSync = {},
            onCompleteActiveAsSuccess = {},
            onCompleteActiveAsTimeout = {},
            onApplyNetworkResult = {}
        )
    }
}

@Preview(
    name = "Dashboard - Failure Heavy",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun DashboardFailureHeavyPreview() {
    PulseSyncTheme {
        DashboardScreen(
            state = DashboardPreviewData.failureHeavyState,
            onStartNextSync = {},
            onCompleteActiveAsSuccess = {},
            onCompleteActiveAsTimeout = {},
            onApplyNetworkResult = {}


        )
    }
}

private fun OperationStatus.toStatusTone(): StatusTone {
    return when (this) {
        OperationStatus.Pending -> StatusTone.Warning
        OperationStatus.Syncing -> StatusTone.Info
        OperationStatus.Synced -> StatusTone.Success
        OperationStatus.Failed -> StatusTone.Error
    }
}

private fun SyncHealthStatus.toStatusTone(): StatusTone {
    return when (this) {
        SyncHealthStatus.Healthy -> StatusTone.Success
        SyncHealthStatus.Degraded -> StatusTone.Warning
        SyncHealthStatus.Failing -> StatusTone.Error
        SyncHealthStatus.Offline -> StatusTone.Neutral
    }
}