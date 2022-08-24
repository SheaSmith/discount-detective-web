package nz.shea.discountdetective.api.repositories

import nz.shea.discountdetective.api.data.StorageProduct
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ProductRepository : CrudRepository<StorageProduct, String> {
    @Query("SELECT product FROM StorageProduct product JOIN product.information info WHERE info.name LIKE '%:query%' OR info.brandName LIKE '%:query%' OR info.variant LIKE '%:query%'")
    fun searchProducts(query: String): List<StorageProduct>
}