package com.example.cosc345project.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class Navigation(
    val route: String,
    val nameText: String?,
    val icon: ImageVector?
) {
    SEARCH("search", "Search", Icons.Rounded.Search),
    PRODUCT("products/{productId}", null, null),
    SHOPPING_LIST("list", "Shopping List", Icons.Rounded.List);
//    SETTINGS("settings", R.string.settings, Icons.Rounded.Settings);

    companion object {
        val topLevel = arrayOf(SEARCH)
    }
}