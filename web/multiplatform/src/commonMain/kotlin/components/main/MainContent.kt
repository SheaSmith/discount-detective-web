package com.example.cosc345project.ui.components.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cosc345project.ui.Navigation
import com.example.cosc345project.ui.screens.ProductScreen
import com.example.cosc345project.ui.screens.SearchScreen
import com.example.cosc345project.ui.screens.ShoppingListScreen
import com.example.cosc345project.ui.utils.NavigationType
import kotlinx.coroutines.channels.Channel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.ui.viewModel
import viewmodels.ProductViewModel
import viewmodels.SearchViewModel
import viewmodels.ShoppingListViewModel

/**
 * The main content of the page, including the screens themselves.
 *
 * @param navController The nav controller to use when navigating between pages.
 * @param navigationType The type of navigation that is being used, based on screen size.
 * @param onDrawerClicked The function to be called when the open drawer button is clicked.
 */
@Composable
fun MainContent(
    navController: Navigator,
    navigationType: NavigationType,
    scrollListener: Channel<Double>?,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == NavigationType.NAVIGATION_RAIL) {
            MainNavigationRail(
                navController,
                onDrawerClicked = onDrawerClicked
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Surface(modifier = Modifier.weight(1.0f)) {
                NavHost(
                    navigator = navController,
                    initialRoute = Navigation.SEARCH.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    scene(Navigation.SEARCH.route) {
                        val parentViewModel = viewModel {
                            SearchViewModel()
                        }
                        SearchScreen(parentViewModel, navController, scrollListener)
                    }
                    scene(Navigation.SHOPPING_LIST.route) {
                        val parentViewModel = viewModel {
                            ShoppingListViewModel()
                        }
                        ShoppingListScreen(parentViewModel, scrollListener)
                    }
                    scene(
                        Navigation.PRODUCT.route
                    ) {
                        ProductScreen(
                            it.path("productId")!!,
                            nav = navController,
                            scrollListener = scrollListener,
                            viewModel = viewModel { ProductViewModel() })
                    }
                }
            }

            AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAVIGATION) {
//                Surface(
//                    color = NavigationBarDefaults.containerColor,
//                    tonalElevation = NavigationBarDefaults.Elevation
//                ) {
                MainBottomNavigationBar(navController)
//                }
            }
        }
    }
}