@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cosc345project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cosc345project.ui.components.product.ProductTitle
import components.AsyncImage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import models.Retailer
import models.RetailerProductInformation
import models.StorePricingInformation
import moe.tlaster.precompose.navigation.Navigator
import viewmodels.ProductViewModel

/**
 * Function used to create the image for the Product on the Product Screen.
 *
 * @param image The image from the Product information to be displayed.
 */
@Composable
fun ProductImage(image: String?) {
    println(image)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(bottom = 10.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            url = image,
            contentDescription = null,
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.FillHeight,
        )
    }
}

/**
 * Function used to create the retailer slot which shows the prices for the products.
 *
 * Includes both the regular price and the discounted price.
 */
@Composable
fun RetailerSlot(
    pricingInformation: StorePricingInformation,
    retailer: Retailer,
    productInformation: RetailerProductInformation
) {
    val store = retailer.stores!!.first { it.id == pricingInformation.store }

    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp
    ) {
        Row {
            var showRetailer by remember { mutableStateOf(false) }




            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically)
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight()
                        .padding(end = 10.dp),
                    //.width(intrinsicSize = IntrinsicSize.Min)

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .width(45.dp)
                            .height(45.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                showRetailer = !showRetailer
                            },
                        shape = CircleShape,
                        color = Color(if (isSystemInDarkTheme()) retailer.colourDark!! else retailer.colourLight!!)
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = retailer.initialism!!,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(if (isSystemInDarkTheme()) retailer.onColourDark!! else retailer.onColourLight!!)
                            )
                        }

//                DropdownMenu(expanded = showRetailer, onDismissRequest = { showRetailer = false }) {
//                    DropdownMenuItem(
//                        text = { Text(retailer.name!!) },
//                        onClick = { },
//                        colors = MenuDefaults.itemColors(disabledTextColor = MaterialTheme.colorScheme.onSurface),
//                        enabled = false
//                    )
//                }
                    }

                    Spacer(Modifier.width(10.0.dp))

                    Text(
                        text = store.name!!,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        maxLines = 3,

                        )
                }

            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            ) {
                val price = pricingInformation.price?.let {
                    pricingInformation.getDisplayPrice(
                        productInformation,
                        it
                    )
                }

                Text(
                    text = price?.let { "${price.first}${price.second}" } ?: "",
                    fontSize = 14.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .align(Alignment.CenterVertically)
            ) {
                if (pricingInformation.discountPrice != null) {
                    val price = pricingInformation.getDisplayPrice(
                        productInformation,
                        pricingInformation.discountPrice!!
                    )

                    Text(
                        text = "${price.first}${price.second}",
                        fontSize = 14.sp
                    )
                }

                if (pricingInformation.multiBuyPrice != null && pricingInformation.multiBuyQuantity != null) {
                    val price = pricingInformation.getDisplayPrice(
                        productInformation,
                        pricingInformation.multiBuyPrice!!
                    )

                    Text(
                        text = "${pricingInformation.multiBuyQuantity} for ${price.first}${price.second}",
                        fontSize = 14.sp
                    )
                }

                if (pricingInformation.clubOnly == true) {
                    Text(
                        text = "Club only",

                        lineHeight = 14.sp,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

///**
// * Function used to handle adding the product to the shopping list.
// *
// * @param product The product selected to be added to the shopping list.
// * @param snackbarHostState The passed down SnackbarHostState.
// * @param retailers List of retailers who are selling the product.
// */
//@Composable
//fun ProductInformation(
//    product: Pair<String, Product>?,
//    snackbarHostState: SnackbarHostState,
//    retailers: Map<String, Retailer>?,
//    viewModel: ProductViewModel
//) {
//    val bestInformation = product?.second?.getBestInformation()
//    val loading = product == null
//
//    Column(
//        modifier = Modifier
//            .padding(start = 10.dp, end = 10.dp)
//    )
//    {
//
//        AddToShoppingListBlock(
//            snackbarHostState = snackbarHostState,
//            productPair = product,
//            retailers = retailers,
//            loading = loading,
//            onAddToShoppingList = { productId, retailerProductInfoId, storeId, quantity ->
//                viewModel.addToShoppingList(productId, retailerProductInfoId, storeId, quantity)
//            }
//        )
//    }
//
//
//}

/**
 * Function used to layout the main content of the product screen page.
 *
 * @param productId A string representing the id of the product.
 * @param viewModel Instance of the SearchViewModel class (see [com.example.cosc345project.viewmodel.SearchViewModel])
 * @param nav Instance of the nav controller for navigation class.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    productId: String,
    viewModel: ProductViewModel,
    nav: Navigator,
    scrollListener: Channel<Double>?
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarScrollState()
    )

    viewModel.getProduct(productId)

    val snackbarHostState = remember { SnackbarHostState() }

    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val retailers by viewModel.retailers.collectAsState()
    val product by viewModel.product.collectAsState()
    val localRetailerProductInformation by viewModel.localRetailerInfo.collectAsState()
    val nonLocalRetailerProductInformation by viewModel.nonLocalRetailerInfo.collectAsState()

    LaunchedEffect(Unit) {
        scrollListener?.receiveAsFlow()?.collect {
            state.scrollBy(it.toFloat())
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            LargeTopAppBar(
                title = {
                    ProductTitle(
                        info = product?.second?.getBestInformation(),
                        loading = product == null,
                        applyStyling = false
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        nav.goBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },

        content = { padding ->
            LazyColumn(contentPadding = padding, state = state, modifier = Modifier.draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        state.scrollBy(-delta)
                    }
                }
            )) {
                item {
                    ProductImage(image = product?.second?.getBestInformation()?.image)
                }

//                item {
//                    ProductInformation(
//                        product = product,
//                        snackbarHostState = snackbarHostState,
//                        retailers = retailers,
//                        viewModel = viewModel
//                    )
//                }

                if (localRetailerProductInformation.isNotEmpty() && retailers.isNotEmpty()) {
                    item {
                        TableHeader(true)
                    }

                    //var sortedInformation = localRetailerProductInformation.sortedByDescending { it.pricing }

                    val sortedList = localRetailerProductInformation.flatMap { info ->
                        info.pricing!!.map { info to it }
                    }.sortedBy {
                        it.second.getPrice(it.first)
                    }

                    items(sortedList) { item ->
                        RetailerSlot(
                            pricingInformation = item.second,
                            retailer = retailers[item.first.retailer]!!,
                            productInformation = item.first
                        )

                    }
                }

                val sortedList = nonLocalRetailerProductInformation.flatMap { info ->
                    info.pricing!!.map { info to it }
                }.sortedBy {
                    it.second.getPrice(it.first)
                }


                if (nonLocalRetailerProductInformation.isNotEmpty() && retailers.isNotEmpty()) {
                    item {
                        TableHeader(false)
                    }

                    items(sortedList) { item ->
                        RetailerSlot(
                            pricingInformation = item.second,
                            retailer = retailers[item.first.retailer]!!,
                            productInformation = item.first
                        )
                    }
                }

            }
        }
    )

}

/**
 * Function used to create the headers (local/non-local, retailer, price, discount price) above the
 * lists of cards with their retailer and price.
 *
 * @param local Whether or not the section is for local products.
 */
@Composable
fun TableHeader(local: Boolean) {
    Text(
        text = if (local) "Local Prices" else "Non-Local Prices",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 16.dp, bottom = 4.dp)

    )

    Row(
        modifier = Modifier.padding(all = 10.dp),
        verticalAlignment = Alignment.Bottom
    )
    {
        Text(
            text = "Retailer",
            modifier = Modifier.weight(1.0f),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Price",
            modifier = Modifier.weight(0.5f),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Discount Price",
            modifier = Modifier.weight(0.5f),
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}