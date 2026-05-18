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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurface

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter, onClick = {

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