package com.paulmathew.pulsesync.navigation

sealed class PulseRoute(
    val route: String
) {

    data object Dashboard : PulseRoute("dashboard")

    data object Timeline : PulseRoute("timeline")

    data object Queue : PulseRoute("queue")

    data object Simulation : PulseRoute("simulation")
}