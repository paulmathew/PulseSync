package com.paulmathew.pulsesync.ui.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paulmathew.pulsesync.ui.components.OperationalPanel
import com.paulmathew.pulsesync.ui.components.TimelineEventRow
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary

@Composable
fun TimelineRoute() {
    TimelineScreen(
        state = TimelinePreviewData.defaultState
    )
}

@Composable
fun TimelineScreen(
    state: TimelineUiState,
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
        TimelineHeader()

        TimelineFilterStrip(
            filters = state.filters,
            selectedFilter = state.selectedFilter
        )

        TimelineEventList(
            state = state
        )
    }
}

@Composable
private fun TimelineHeader() {
    Column {
        Text(
            text = "Timeline",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Chronological sync lifecycle events",
            color = TextSecondary
        )
    }
}

@Composable
private fun TimelineFilterStrip(
    filters: List<TimelineFilter>,
    selectedFilter: TimelineFilter
) {

}

@Composable
private fun TimelineEventList(
    state: TimelineUiState
) {
    OperationalPanel {
        state.events.forEach { event ->
            TimelineEventRow(event = event)
        }
    }
}

@Preview(
    name = "Timeline",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun TimelineScreenPreview() {
    PulseSyncTheme {
        TimelineScreen(
            state = TimelinePreviewData.defaultState
        )
    }
}