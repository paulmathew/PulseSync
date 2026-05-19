package com.paulmathew.pulsesync.sync.runtime

import com.paulmathew.pulsesync.model.NetworkProfile
import com.paulmathew.pulsesync.model.NetworkProfileType

data class NetworkSimulationRuntimeState(
    val selectedProfile: NetworkProfile,
    val profiles: List<NetworkProfile>,
    val isSimulationRunning: Boolean
) {
    companion object {
        val Default = NetworkSimulationRuntimeState(
            selectedProfile = NetworkProfiles.goodNetwork,
            profiles = NetworkProfiles.all,
            isSimulationRunning = false
        )
    }
}

object NetworkProfiles {

    val goodNetwork = NetworkProfile(
        type = NetworkProfileType.Good,
        name = "Good Network",
        description = "20 ms · 0% loss",
        latencyMs = 20,
        packetLossPercent = 0,
        timeoutMs = null,
        isOffline = false
    )

    val all = listOf(
        goodNetwork,
        NetworkProfile(
            type = NetworkProfileType.Slow,
            name = "Slow Network",
            description = "2s delay · 0% loss",
            latencyMs = 2_000,
            packetLossPercent = 0,
            timeoutMs = null,
            isOffline = false
        ),
        NetworkProfile(
            type = NetworkProfileType.Poor,
            name = "Poor Network",
            description = "5s delay · 5% loss",
            latencyMs = 5_000,
            packetLossPercent = 5,
            timeoutMs = null,
            isOffline = false
        ),
        NetworkProfile(
            type = NetworkProfileType.PacketLoss,
            name = "Packet Loss",
            description = "300 ms · 20% loss",
            latencyMs = 300,
            packetLossPercent = 20,
            timeoutMs = null,
            isOffline = false
        ),
        NetworkProfile(
            type = NetworkProfileType.Timeout,
            name = "Timeout",
            description = "10s timeout",
            latencyMs = 10_000,
            packetLossPercent = 0,
            timeoutMs = 10_000,
            isOffline = false
        ),
        NetworkProfile(
            type = NetworkProfileType.Offline,
            name = "Offline",
            description = "No internet connection",
            latencyMs = 0,
            packetLossPercent = 100,
            timeoutMs = null,
            isOffline = true
        )
    )
}