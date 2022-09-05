package com.example.cosc345project.ui.components.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cosc345project.ui.Navigation
import com.example.cosc345project.ui.utils.NavigationType
import moe.tlaster.precompose.navigation.Navigator

/**
 * The content of the navigation drawer.
 *
 * @param navController The nav controller to use when navigating between pages.
 * @param modifier The modifier to use for the content of the navigation drawer.
 * @param onDrawerClicked The function to be called when the open drawer button is clicked.
 * @param navigationType The type of navigation that is being used, based on screen size.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    navController: Navigator,
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {},
    navigationType: NavigationType
) {
    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Discount Detective".uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            AnimatedVisibility(visible = navigationType != NavigationType.PERMANENT_NAVIGATION_DRAWER) {
                IconButton(onClick = onDrawerClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Navigation Drawer"
                    )
                }
            }

        }

        val navBackStackEntry by navController.currentEntry.collectAsState(null)
        val currentDestination = navBackStackEntry

        Navigation.topLevel.forEach { screen ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = screen.nameText!!,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                selected = currentDestination?.route?.route == screen.route,
                onClick = { handleNavigationClick(screen, navController) },
                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
            )
        }
    }
}