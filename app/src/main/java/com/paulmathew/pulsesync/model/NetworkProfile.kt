package com.paulmathew.pulsesync.model

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