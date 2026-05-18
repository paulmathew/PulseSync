package com.paulmathew.pulsesync.ui.simulation

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
import com.paulmathew.pulsesync.model.NetworkProfile
import com.paulmathew.pulsesync.ui.components.OperationalPanel
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.PulseSyncTheme
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary

@Composable
fun NetworkSimulationRoute() {
    NetworkSimulationScreen(
        state = NetworkSimulationPreviewData.defaultState,
        onProfileSelected = {},
        onStartSimulation = {}
    )
}

@Composable
fun NetworkSimulationScreen(
    state: NetworkSimulationUiState,
    onProfileSelected: (NetworkProfile) -> Unit,
    onStartSimulation: () -> Unit,
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
        NetworkSimulationHeader()

        NetworkProfileSection(
            profiles = state.profiles,
            selectedProfile = state.selectedProfile,
            onProfileSelected = onProfileSelected
        )

        CustomSettingsSection(
            settings = state.customSettings
        )

        SimulationActionSection(
            selectedProfile = state.selectedProfile,
            isSimulationRunning = state.isSimulationRunning,
            onStartSimulation = onStartSimulation
        )
    }
}

@Composable
private fun NetworkSimulationHeader() {
    Column {
        Text(
            text = "Network Simulation",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Inject unreliable network behavior into the sync pipeline",
            color = TextSecondary
        )
    }
}

@Composable
private fun NetworkProfileSection(
    profiles: List<NetworkProfile>,
    selectedProfile: NetworkProfile,
    onProfileSelected: (NetworkProfile) -> Unit
) {
    OperationalPanel {
        Text(
            text = "Network Profile",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CustomSettingsSection(
    settings: NetworkSimulationSettings
) {
    OperationalPanel {
        Text(
            text = "Custom Settings",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SimulationActionSection(
    selectedProfile: NetworkProfile,
    isSimulationRunning: Boolean,
    onStartSimulation: () -> Unit
) {
    OperationalPanel {
        Text(
            text = "Current: ${selectedProfile.name}",
            color = TextSecondary
        )
    }
}

@Preview(
    name = "Network Simulation",
    showBackground = true,
    backgroundColor = 0xFF0D1117
)
@Composable
private fun NetworkSimulationScreenPreview() {
    PulseSyncTheme {
        NetworkSimulationScreen(
            state = NetworkSimulationPreviewData.defaultState,
            onProfileSelected = {},
            onStartSimulation = {}
        )
    }
}