package viewmodels

import com.example.cosc345.shared.models.Retailer
import com.example.cosc345.shared.models.RetailerProductInformation
import com.example.cosc345.shared.models.StorePricingInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class ShoppingListViewModel : ViewModel() {

    val products: MutableStateFlow<List<Triple<RetailerProductInformation, StorePricingInformation, Int>>?> =
        MutableStateFlow(null)

    val retailers = MutableStateFlow<Map<String, Retailer>>(mapOf())

    /**
     * For a given RetailerProductInfo in dao
     * Return the associated store namze
     *
     * have retailer ID
     *
     * If store name and retailer name same just return store
     */
    fun getStoreName(
        retailerId: String,
        storeId: String,
        retailers: Map<String, Retailer>
    ): String? {
        val retailer = retailers[retailerId]

        val store = retailer?.stores?.firstOrNull { it.id == storeId }

        if (store?.name == retailer?.name) {
            return retailer?.name
        }

        if (retailer != null && store != null)
            return "${retailer.name} ${store.name}"

        return null
    }
}