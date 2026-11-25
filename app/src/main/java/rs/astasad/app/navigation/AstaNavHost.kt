package rs.astasad.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rs.astasad.app.features.activities.ActivitiesScreen
import rs.astasad.app.features.auth.LoginScreen
import rs.astasad.app.features.forum.ForumScreen
import rs.astasad.app.features.profile.ProfileScreen
import rs.astasad.app.ui.components.AstaAppScaffold

@Composable
fun AstaNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = AstaDestination.entries.firstOrNull {
        it.route == backStackEntry?.destination?.route
    }

    val openTab: (AstaDestination) -> Unit = { destination ->
        if (navController.currentDestination?.route != destination.route) {
            navController.navigate(destination.route) {
                popUpTo(LOGIN_ROUTE)
                launchSingleTop = true
            }
        }
    }

    AstaAppScaffold(
        title = selectedTab?.let { stringResource(it.label) },
        bottomBar = {
            if (selectedTab != null) {
                AstaNavigationBar(selectedTab, openTab)
            }
        }
    ) { modifier ->
        NavHost(
            navController = navController,
            startDestination = LOGIN_ROUTE,
            modifier = modifier.fillMaxSize()
        ) {
            composable(LOGIN_ROUTE) {
                LoginScreen(onContinueDemo = { openTab(AstaDestination.Activities) })
            }
            composable(AstaDestination.Activities.route) { ActivitiesScreen() }
            composable(AstaDestination.Forum.route) { ForumScreen() }
            composable(AstaDestination.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
private fun AstaNavigationBar(
    selectedTab: AstaDestination,
    onTabSelected: (AstaDestination) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    NavigationBar(containerColor = colors.surface) {
        AstaDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination == selectedTab,
                onClick = { onTabSelected(destination) },
                icon = { Icon(painterResource(destination.icon), contentDescription = null) },
                label = { Text(stringResource(destination.label)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    selectedTextColor = colors.primary,
                    indicatorColor = colors.primaryContainer
                )
            )
        }
    }
}
