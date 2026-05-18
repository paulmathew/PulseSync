package com.paulmathew.pulsesync.ui.simulation

import com.paulmathew.pulsesync.model.NetworkProfile

data class NetworkSimulationUiState(
    val selectedProfile: NetworkProfile,
    val profiles: List<NetworkProfile>,
    val customSettings: NetworkSimulationSettings,
    val isSimulationRunning: Boolean
)

data class NetworkSimulationSettings(
    val latencyMs: Int,
    val packetLossPercent: Int,
    val timeoutLabel: String
)