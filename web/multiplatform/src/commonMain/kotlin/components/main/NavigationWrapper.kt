package com.example.cosc345project.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.cosc345project.ui.utils.NavigationType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.rememberNavigator

/**
 * The wrapper used to create the drawer navigation.
 *
 * @param navigationType The type of navigation that is being used, based on screen size.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper(
    navigationType: NavigationType,
    scrollListener: Channel<Double>?
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavigator()

    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(
            drawerContent = {
                NavigationDrawerContent(
                    navController,
                    navigationType = navigationType
                )
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            MainContent(navController, navigationType, scrollListener)
        }
    } else {
        // Apparently we need the explicit type so Android Studio doesn't try to say it's not needed
        @Suppress("RedundantExplicitType") val modifier: Modifier = Modifier

        ModalNavigationDrawer(
            drawerContent = {
                NavigationDrawerContent(
                    navController,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    navigationType = navigationType
                )
            },
            drawerState = drawerState,
            modifier = modifier
        ) {
            MainContent(
                navController,
                navigationType,
                scrollListener,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}