package com.paulmathew.pulsesync.model

import com.paulmathew.pulsesync.ui.components.StatusTone

data class NetworkProfile(
    val type: NetworkProfileType,
    val name: String,
    val description: String,
    val latencyMs: Int,
    val packetLossPercent: Int,
    val timeoutMs: Int?,
    val isOffline: Boolean
)

enum class NetworkProfileType {
    Good,
    Slow,
    Poor,
    PacketLoss,
    Timeout,
    Offline
}

val NetworkProfileType.symbol: String
    get() = when (this) {
        NetworkProfileType.Good -> "✓"
        NetworkProfileType.Slow -> "↯"
        NetworkProfileType.Poor -> "!"
        NetworkProfileType.PacketLoss -> "%"
        NetworkProfileType.Timeout -> "⏱"
        NetworkProfileType.Offline -> "⦸"
    }

fun NetworkProfileType.toStatusTone(): StatusTone {
    return when (this) {
        NetworkProfileType.Good -> StatusTone.Success
        NetworkProfileType.Slow -> StatusTone.Warning
        NetworkProfileType.Poor -> StatusTone.Error
        NetworkProfileType.PacketLoss -> StatusTone.Warning
        NetworkProfileType.Timeout -> StatusTone.Error
        NetworkProfileType.Offline -> StatusTone.Neutral
    }
}