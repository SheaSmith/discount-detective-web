package com.example.cosc345project.ui.components.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cosc345project.ui.Navigation
import moe.tlaster.precompose.navigation.Navigator

/**
 * The bottom navigation bar used on phones.
 *
 * @param navController The nav controller to use when navigating between pages.
 */
@Composable
fun MainBottomNavigationBar(navController: Navigator) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentEntry.collectAsState(initial = null)
        Navigation.topLevel.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = null
                    )
                },
                label = { Text(screen.nameText!!) },
                selected = navBackStackEntry?.route?.route == screen.route,
                onClick = { handleNavigationClick(screen, navController) }
            )
        }

    }
}