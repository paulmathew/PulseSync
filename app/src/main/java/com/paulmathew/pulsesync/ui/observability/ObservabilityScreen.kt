package com.paulmathew.pulsesync.ui.observability

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paulmathew.pulsesync.ui.components.MetricTile
import com.paulmathew.pulsesync.ui.components.OperationalPanel
import com.paulmathew.pulsesync.ui.components.StatusTone
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary

@Composable
fun ObservabilityRoute(
    viewModel: ObservabilityViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObservabilityScreen(state = state)
}

@Composable
fun ObservabilityScreen(
    state: ObservabilityUiState,
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
        ObservabilityHeader()

        MetricGrid(state = state)

        FailureReasonsPanel(
            failureReasons = state.failureReasons
        )
    }
}

@Composable
private fun ObservabilityHeader() {
    Column {
        Text(
            text = "Observability",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Runtime health metrics derived from sync events",
            color = TextSecondary
        )
    }
}

@Composable
private fun MetricGrid(
    state: ObservabilityUiState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricTile(
                label = "Success Rate",
                value = state.successRateLabel,
                tone = StatusTone.Success,
                modifier = Modifier.weight(1f)
            )

            MetricTile(
                label = "Events",
                value = state.totalEvents.toString(),
                tone = StatusTone.Info,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricTile(
                label = "Failed",
                value = state.failureCount.toString(),
                tone = StatusTone.Error,
                modifier = Modifier.weight(1f)
            )

            MetricTile(
                label = "Retries",
                value = state.retryScheduledCount.toString(),
                tone = StatusTone.Warning,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FailureReasonsPanel(
    failureReasons: List<FailureReasonMetric>
) {
    OperationalPanel {
        Text(
            text = "Failure Reasons",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        if (failureReasons.isEmpty()) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "No failures recorded",
                color = TextSecondary
            )
        } else {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                failureReasons.forEach { reason ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = reason.label,
                            color = TextSecondary
                        )

                        Text(
                            text = reason.count.toString(),
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Observability",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun ObservabilityScreenPreview() {
    PulseSyncTheme {
        ObservabilityScreen(
            state = ObservabilityUiState(
                totalEvents = 12,
                successCount = 7,
                failureCount = 3,
                retryScheduledCount = 2,
                successRateLabel = "70%",
                failureReasons = listOf(
                    FailureReasonMetric("Timeout", 2),
                    FailureReasonMetric("Offline", 1)
                )
            )
        )
    }
}