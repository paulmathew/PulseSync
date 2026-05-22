package com.paulmathew.pulsesync.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paulmathew.pulsesync.ui.activity.ActivityRoute
import com.paulmathew.pulsesync.ui.conflict.ConflictResolutionRoute
import com.paulmathew.pulsesync.ui.dashboard.DashboardRoute
import com.paulmathew.pulsesync.ui.diagnostics.DiagnosticsHomeRoute
import com.paulmathew.pulsesync.ui.editor.EditorRoute
import com.paulmathew.pulsesync.ui.observability.ObservabilityRoute
import com.paulmathew.pulsesync.ui.queue.QueueRoute
import com.paulmathew.pulsesync.ui.simulation.NetworkSimulationRoute
import com.paulmathew.pulsesync.ui.timeline.TimelineRoute
import com.paulmathew.pulsesync.ui.home.WorkspaceHomeRoute
import com.paulmathew.pulsesync.ui.profile.ProfileRoute
import com.paulmathew.pulsesync.ui.theme.PulseColors


@Composable
fun PulseNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = PulseRoute.Home.route,
        modifier = modifier
    ) {
        composable(PulseRoute.Home.route) {
            WorkspaceHomeRoute(
                onWorkspaceClick = { item ->
                    navController.navigate(
                        PulseRoute.Editor.createRoute(item.id)
                    )
                }
            )
        }

        composable(PulseRoute.Activity.route) {
            ActivityRoute()
        }

        composable(PulseRoute.Create.route) {
            PlaceholderRoute(title = "Create\n" +
                    "New workspace actions are coming next.")
        }

        composable(PulseRoute.Shared.route) {
            PlaceholderRoute(title = "Shared\n" +
                    "Collaborative spaces will appear here.")
        }

        composable(PulseRoute.Profile.route) {
            ProfileRoute(
                onDeveloperDiagnosticsClick = {
                    navController.navigate(PulseRoute.DiagnosticsHome.route)
                }
            )
        }
//        composable(PulseRoute.Editor.route) {
//            EditorRoute(
//                onBackClick = { navController.popBackStack() }
//            )
//        }
        composable(PulseRoute.Editor.route) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId")

            EditorRoute(
                documentId = documentId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(PulseRoute.DiagnosticsHome.route) {
            DiagnosticsHomeRoute(
                onNetworkClick = {
                    navController.navigate(PulseRoute.DiagnosticsNetwork.route)
                },
                onRuntimeEventsClick = {
                    navController.navigate(PulseRoute.DiagnosticsRuntimeEvents.route)
                },
                onObservabilityClick = {
                    navController.navigate(PulseRoute.DiagnosticsObservability.route)
                },
                onQueueClick = {
                    navController.navigate(PulseRoute.DiagnosticsQueue.route)
                },
                onConflictDebugClick = {
                    navController.navigate(PulseRoute.DiagnosticsConflicts.route)
                }
            )
        }
    }
}

@Composable
private fun PlaceholderRoute(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PulseColors.BackgroundPrimary)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = PulseColors.TextPrimary,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}