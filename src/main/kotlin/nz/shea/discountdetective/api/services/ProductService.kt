package nz.shea.discountdetective.api.services

import com.example.cosc345.shared.models.Product
import nz.shea.discountdetective.api.data.StorageProduct
import nz.shea.discountdetective.api.data.StorageRetailer
import nz.shea.discountdetective.api.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val db: ProductRepository) {
    fun searchProducts(query: String): Map<String, Product> = db.searchProducts(query).associate { it.toProduct() }

    fun addAll(products: Map<String, Product>) =
        db.saveAll(products.map { StorageProduct(it.value, it.key) })
}