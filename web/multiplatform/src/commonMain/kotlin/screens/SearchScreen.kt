@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cosc345project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.example.cosc345project.ui.components.search.SearchProductCard
import com.example.cosc345project.ui.components.search.SearchTopAppBar
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import viewmodels.SearchViewModel

/**
 * Function for the Search Screen
 *
 * Creates the user interface search screen and links elements of the UI to the database so that
 * users can search for certain foods.
 *
 * @param viewModel Instance of the SearchViewModel class (see [com.example.cosc345project.viewmodel.SearchViewModel])
 * @param navController Instance of the nav controller for navigation class.
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: Navigator,
    scrollListener: Channel<Double>?
) {
    val search by viewModel.searchQuery.collectAsState()
    val retailers by viewModel.retailers.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    //val searchResults by viewModel.searchLiveData
    //val productResults = searchResults.collectAsLazyPagingItems()
    var loading by remember {
        mutableStateOf(false)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarScrollState()
    )
    val focusManager = LocalFocusManager.current
    var showSuggestions by remember {
        viewModel.showSuggestions
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,
                title = {
                    SearchTopAppBar(
                        search = search,
                        loading = loading,
                        onValueChange = {
                            val query = it.filter { letter -> letter != '\n' }
                            viewModel.setQuery(query)
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        },
                        onFocusChanged = {
                            showSuggestions = it.isFocused
                        },
                        onSearch = {
                            viewModel.query()
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        },
                        onClear = {
                            viewModel.setQuery("", runSearch = true)
                            coroutineScope.launch {
                                listState.scrollToItem(0)
                            }
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LaunchedEffect(Unit) {
            scrollListener?.receiveAsFlow()?.collect {
                println(it)
                listState.scrollBy(it.toFloat())
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                innerPadding.calculateTopPadding() + 4.dp,
                innerPadding.calculateEndPadding(
                    LocalLayoutDirection.current
                ),
                innerPadding.calculateBottomPadding() + 8.dp
            ),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { focusManager.clearFocus() }
                    )
                }.draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            listState.scrollBy(-delta)
                        }
                    }
                ),
            state = listState
        ) {
            //val loadState = productResults.loadState.refresh
            //loading = loadState == LoadState.Loading

            if (showSuggestions && suggestions.isNotEmpty()) {
                item {
                    Text(
                        text = "Search Suggestions",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                items(
                    items = suggestions
                ) {
                    ListItem(
                        headlineText = { Text(text = it) },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                viewModel.setQuery(it, true)
                                focusManager.clearFocus()
                            }
                    )
                }
            } else {

                if (true) {
                    items(10) {
                        SearchProductCard(
                            null,
                            false,
                            navController,
                            retailers,
                            snackbarHostState,
                            coroutineScope
                        )
                    }
                }
//                else if (retailers.isNotEmpty() && productResults.itemCount != 0) {
//                    items(
//                        items = productResults,
//                        key = { product -> product.first }
//                    ) {
//                        SearchProductCard(
//                            it,
//                            loading,
//                            navController,
//                            retailers,
//                            snackbarHostState,
//                            coroutineScope,
//                            onAddToShoppingList = { productId, retailerProductInfoId, storeId, quantity ->
//                                viewModel.addToShoppingList(
//                                    productId,
//                                    retailerProductInfoId,
//                                    storeId,
//                                    quantity
//                                )
//                            }
//                        )
//                    }
//                } else if (loadState !is LoadState.Error && retailers.isNotEmpty()) {
//                    item {
//                        SearchError(
//                            title = R.string.no_results,
//                            description = R.string.no_results_description,
//                            icon = Icons.Rounded.SearchOff
//                        )
//                    }
//                } else {
//                    item {
//                        SearchError(
//                            title = R.string.no_internet,
//                            description = R.string.no_internet_description,
//                            icon = Icons.Rounded.SignalWifiOff,
//                            onRetry = {
//                                viewModel.query()
//                            })
//                    }
//                }
            }
        }

    }
}