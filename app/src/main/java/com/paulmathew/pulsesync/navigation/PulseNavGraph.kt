package com.paulmathew.pulsesync.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paulmathew.pulsesync.ui.dashboard.DashboardRoute
import com.paulmathew.pulsesync.ui.observability.ObservabilityRoute
import com.paulmathew.pulsesync.ui.queue.QueueRoute
import com.paulmathew.pulsesync.ui.simulation.NetworkSimulationRoute
import com.paulmathew.pulsesync.ui.timeline.TimelineRoute


@Composable
fun PulseNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = PulseRoute.Dashboard.route,
        modifier = modifier
    ) {
        composable(PulseRoute.Dashboard.route) {
            DashboardRoute()
        }

        composable(PulseRoute.Timeline.route) {
            TimelineRoute()
        }

        composable(PulseRoute.Queue.route) {
            QueueRoute()
        }

        composable(PulseRoute.NetworkSimulation.route) {
            NetworkSimulationRoute()
        }

        composable(PulseRoute.Observability.route) {
            ObservabilityRoute()
        }
    }
}