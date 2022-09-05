package viewmodels

import androidx.compose.runtime.mutableStateOf
import com.example.cosc345.shared.models.Retailer
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class SearchViewModel : ViewModel() {
    /**
     * The current search query.
     */
    val searchQuery = MutableStateFlow("")

    /**
     * The current search suggestions for the current query.
     */
    val suggestions = MutableStateFlow<List<String>>(listOf())

    /**
     * Whether suggestions should be shown or not.
     */
    val showSuggestions = mutableStateOf(false)

    /**
     * The retailers currently stored in Firebase.
     */
    val retailers = MutableStateFlow<Map<String, Retailer>>(mapOf())

    fun query() {

    }

    fun setQuery(query: String, runSearch: Boolean = false) {

    }
}