package com.example.cosc345project.ui.components.main

import com.example.cosc345project.ui.Navigation
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

/**
 * Handle navigation click, essentially move to the new page.
 *
 * @param screen The screen to move to.
 * @param navHostController The nav host controller to navigate to the new page with.
 */
internal fun handleNavigationClick(screen: Navigation, navHostController: Navigator) {
    navHostController.navigate(screen.route, options = NavOptions(true, PopUpTo(screen.route)))
}