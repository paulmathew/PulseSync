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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import com.paulmathew.pulsesync.model.NetworkProfileType
import com.paulmathew.pulsesync.model.symbol
import com.paulmathew.pulsesync.model.toStatusTone
import com.paulmathew.pulsesync.ui.components.StatusTone
import com.paulmathew.pulsesync.ui.components.color
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurfaceElevated

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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Network Profile",
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        profiles.forEach { profile ->
            NetworkProfileRow(
                profile = profile,
                selected = profile == selectedProfile,
                onProfileSelected = onProfileSelected
            )
        }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingTile(
                label = "Latency",
                value = "${settings.latencyMs} ms",
                modifier = Modifier.weight(1f)
            )

            SettingTile(
                label = "Packet Loss",
                value = "${settings.packetLossPercent}%",
                modifier = Modifier.weight(1f)
            )

            SettingTile(
                label = "Timeout",
                value = settings.timeoutLabel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@Composable
private fun SettingTile(
    label: String,
    value: String,
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
            .padding(12.dp)
    ) {
        Text(
            text = label,
            color = TextSecondary
        )

        Text(
            text = value,
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
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStartSimulation,
            colors = ButtonDefaults.buttonColors(
                containerColor = OperationalGreen,
                contentColor = GraphiteBackground
            )
        ) {
            Text(
                text = if (isSimulationRunning) "Stop Simulation" else "Start Simulation",
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = "Current: ${selectedProfile.name}",
            color = OperationalGreen
        )
    }
}
@Composable
private fun NetworkProfileRow(
    profile: NetworkProfile,
    selected: Boolean,
    onProfileSelected: (NetworkProfile) -> Unit
) {
    val tone = profile.type.toStatusTone()
    val borderColor = if (selected) OperationalGreen else PanelBorder

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProfileSelected(profile) }
            .background(
                color = PanelSurfaceElevated,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = profile.type.symbol,
            color = tone.color(),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = profile.name,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = profile.description,
                color = TextSecondary
            )
        }

        if (selected) {
            Text(
                text = "ACTIVE",
                color = OperationalGreen,
                fontWeight = FontWeight.SemiBold
            )
        }
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