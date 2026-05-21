package com.paulmathew.pulsesync.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Queue
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.SettingsEthernet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paulmathew.pulsesync.ui.theme.GraphiteBackground
import com.paulmathew.pulsesync.ui.theme.OperationalGreen
import com.paulmathew.pulsesync.ui.theme.PanelBorder
import com.paulmathew.pulsesync.ui.theme.PanelSurface
import com.paulmathew.pulsesync.ui.theme.TextPrimary
import com.paulmathew.pulsesync.ui.theme.TextSecondary
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon

@Composable
fun PulseNavigationShell() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        containerColor = GraphiteBackground,
        bottomBar = {
            PulseBottomNavigationBar(
                selectedRoute = currentDestination?.route,
                onDestinationSelected = { destination ->
                    navController.navigate(destination.route.route) {
                        popUpTo(PulseRoute.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        PulseNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun PulseBottomNavigationBar(
    selectedRoute: String?,
    onDestinationSelected: (PulseTopLevelDestination) -> Unit
) {
    NavigationBar(
        containerColor = PanelSurface,
        contentColor = TextPrimary
    ) {
        PulseTopLevelDestination.entries.forEach { destination ->
            val selected = selectedRoute == destination.route.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    onDestinationSelected(destination)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = {
                    Text(text = destination.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OperationalGreen,
                    selectedTextColor = OperationalGreen,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = PanelBorder
                )
            )
        }
    }
}

private enum class PulseTopLevelDestination(
    val route: PulseRoute,
    val label: String,
    val icon: ImageVector
) {
    Home(
        route = PulseRoute.Home,
        label = "Home",
        icon = Icons.Outlined.Home
    ),
    Activity(
        route = PulseRoute.Activity,
        label = "Activity",
        icon = Icons.Outlined.FormatListBulleted
    ),
    Create(
        route = PulseRoute.Create,
        label = "Create",
        icon = Icons.Outlined.AddCircle
    ),
    Shared(
        route = PulseRoute.Shared,
        label = "Shared",
        icon = Icons.Outlined.Group
    ),
    Profile(
        route = PulseRoute.Profile,
        label = "Profile",
        icon = Icons.Outlined.Person
    )

}

