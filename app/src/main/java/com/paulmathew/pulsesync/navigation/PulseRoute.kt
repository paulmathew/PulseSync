package com.paulmathew.pulsesync.navigation

sealed class PulseRoute(
    val route: String
) {

    data object Dashboard : PulseRoute("dashboard")

    data object Timeline : PulseRoute("timeline")

    data object Queue : PulseRoute("queue")

    data object NetworkSimulation : PulseRoute("network_simulation")

    data object Observability : PulseRoute("observability")

    data object Conflicts : PulseRoute("conflicts")

    data object Home : PulseRoute("home")
    data object Activity : PulseRoute("activity")
    data object Create : PulseRoute("create")
    data object Shared : PulseRoute("shared")
    data object Profile : PulseRoute("profile")
    //data object Editor : PulseRoute("editor")
    data object Editor : PulseRoute("editor/{documentId}") {
        fun createRoute(documentId: String): String {
            return "editor/$documentId"
        }
    }
    data object DiagnosticsHome : PulseRoute("diagnostics")
    data object DiagnosticsNetwork : PulseRoute("diagnostics/network")
    data object DiagnosticsObservability : PulseRoute("diagnostics/observability")
    data object DiagnosticsRuntimeEvents : PulseRoute("diagnostics/runtime-events")
    data object DiagnosticsQueue : PulseRoute("diagnostics/queue")
    data object DiagnosticsConflicts : PulseRoute("diagnostics/conflicts")



}