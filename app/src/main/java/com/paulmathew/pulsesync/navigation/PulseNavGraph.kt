package com.paulmathew.pulsesync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paulmathew.pulsesync.ui.dashboard.DashboardRoute
import com.paulmathew.pulsesync.ui.timeline.TimelineRoute


@Composable
fun PulseNavGraph(){

    val navController = rememberNavController()

    NavHost (
        navController=navController,
        startDestination = PulseRoute.Dashboard.route
    ){
        composable(PulseRoute.Dashboard.route) {
            DashboardRoute()
        }

        composable(PulseRoute.Timeline.route) {
            TimelineRoute()
        }

        composable(PulseRoute.Queue.route) {
            // Coming in next feature branch
        }

        composable(PulseRoute.NetworkSimulation.route) {
            // Coming in next feature branch
        }
    }
}