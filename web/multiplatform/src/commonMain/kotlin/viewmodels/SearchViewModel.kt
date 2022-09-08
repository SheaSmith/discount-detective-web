package viewmodels

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import models.Product
import models.Retailer
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import repository.ProductRepository
import repository.RetailerRepository

class SearchViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val retailerRepository = RetailerRepository()

    /**
     * The current search query.
     */
    val searchQuery = MutableStateFlow("")

    /**
     * The retailers currently stored in Firebase.
     */
    val retailers = MutableStateFlow<Map<String, Retailer>>(mapOf())

    val searchResults = MutableStateFlow<Map<String, Product>>(mapOf())
    val loading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            retailers.value = retailerRepository.getRetailers()
        }

        viewModelScope.launch {
            query()
        }
    }

    fun query() {
        loading.value = true
        viewModelScope.launch {
            searchResults.value = productRepository.search(searchQuery.value).content
            loading.value = false
        }
    }

    fun setQuery(query: String, runSearch: Boolean = false) {
        searchQuery.value = query

        if (runSearch) {
            query()
        }
    }
}