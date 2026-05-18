package com.paulmathew.pulsesync.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary

@Composable
fun DashboardRoute() {
    DashboardScreen(
        state = DashboardPreviewData.healthyState
    )
}

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GraphiteBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "PulseSync",
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Dashboard",
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.healthLabel,
            color = TextPrimary
        )
    }
}

@Composable
private fun DashboardTopBar(){}

@Composable
private fun OverallStatusPanel(){}

@Composable
private fun MetricStrip(){}

@Composable
private fun RecentEventsPanel(){}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    PulseSyncTheme {
        DashboardScreen(
            state = DashboardPreviewData.healthyState
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
            state = DashboardPreviewData.healthyState
        )
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
            state = DashboardPreviewData.degradedState
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
            state = DashboardPreviewData.failureHeavyState
        )
    }
}