package viewmodels

import com.example.cosc345.shared.models.Retailer
import com.example.cosc345.shared.models.RetailerProductInformation
import kotlinx.coroutines.flow.MutableStateFlow
import models.Product
import moe.tlaster.precompose.viewmodel.ViewModel

class ProductViewModel : ViewModel() {
    val product = MutableStateFlow<Pair<String, Product>?>(null)
    val retailers = MutableStateFlow<Map<String, Retailer>>(mapOf())

    val localRetailerInfo = MutableStateFlow(listOf<RetailerProductInformation>())
    val nonLocalRetailerInfo = MutableStateFlow(listOf<RetailerProductInformation>())

    fun addToShoppingList(productId: String, retailerProductInfoId: String, storeId: String, quantity: Int) {

    }

    fun getProduct(productId: String) {

    }
}