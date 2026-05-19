package com.paulmathew.pulsesync.ui.mapping

import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeState
import com.paulmathew.pulsesync.ui.simulation.NetworkSimulationSettings
import com.paulmathew.pulsesync.ui.simulation.NetworkSimulationUiState

fun SyncRuntimeState.toNetworkSimulationUiState(): NetworkSimulationUiState {
    val simulation = networkSimulation

    return NetworkSimulationUiState(
        selectedProfile = simulation.selectedProfile,
        profiles = simulation.profiles,
        customSettings = NetworkSimulationSettings(
            latencyMs = simulation.selectedProfile.latencyMs,
            packetLossPercent = simulation.selectedProfile.packetLossPercent,
            timeoutLabel = simulation.selectedProfile.timeoutMs?.let { "${it / 1_000}s" } ?: "Disabled"
        ),
        isSimulationRunning = simulation.isSimulationRunning
    )
}