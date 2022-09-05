package com.example.cosc345project.ui.components.main

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.cosc345project.ui.Navigation
import moe.tlaster.precompose.navigation.Navigator

/**
 * The navigation rail to use on medium sized devices (e.g. small tablets).
 *
 * @param navController The nav controller to use when navigating between pages.
 * @param onDrawerClicked The function to be called when the open drawer button is clicked.
 */
@Composable
fun MainNavigationRail(
    navController: Navigator,
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        val navBackStackEntry by navController.currentEntry.collectAsState(null)
        val currentDestination = navBackStackEntry

        NavigationRailItem(
            selected = false,
            onClick = onDrawerClicked,
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Navigation Drawer"
                )
            }
        )

        Navigation.topLevel.forEach { screen ->
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = null
                    )
                },
                selected = currentDestination?.route?.route == screen.route,
                onClick = { handleNavigationClick(screen, navController) }
            )
        }
    }
}