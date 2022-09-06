package viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import models.Product
import models.Retailer
import models.RetailerProductInformation
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repository.ProductRepository
import repository.RetailerRepository

class ProductViewModel : ViewModel() {
    val product = MutableStateFlow<Pair<String, Product>?>(null)
    val retailers = MutableStateFlow<Map<String, Retailer>>(mapOf())

    private val productRepository = ProductRepository()
    private val retailersRepository = RetailerRepository()

    val localRetailerInfo = MutableStateFlow(listOf<RetailerProductInformation>())
    val nonLocalRetailerInfo = MutableStateFlow(listOf<RetailerProductInformation>())

    init {
        viewModelScope.launch {
            retailers.value = retailersRepository.getRetailers()

            filterInfo()
        }
    }

    fun getProduct(productId: String) {
        viewModelScope.launch {
            product.value = Pair(productId, productRepository.getProduct(productId))

            filterInfo()
        }
    }

    private fun filterInfo() {
        if (product.value != null) {
            val localRetailers = retailers.value.filter { it.value.local == true }.keys

            if (localRetailers.isNotEmpty()) {
                localRetailerInfo.value =
                    product.value!!.second.information!!.filter { localRetailers.contains(it.retailer) }
                nonLocalRetailerInfo.value =
                    product.value!!.second.information!!.filter { !localRetailers.contains(it.retailer) }
            }
        }
    }
}