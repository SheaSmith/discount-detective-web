package nz.shea.discountdetective.api.services

import com.example.cosc345.shared.models.Product
import nz.shea.discountdetective.api.data.PageDTO
import nz.shea.discountdetective.api.data.StorageProduct
import nz.shea.discountdetective.api.repositories.ProductRepository
import org.hibernate.search.engine.search.query.SearchResult
import org.hibernate.search.mapper.orm.Search
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import kotlin.jvm.optionals.getOrNull


@Service
class ProductService(private val db: ProductRepository, private val entityManager: EntityManager) {
    fun searchProducts(query: String, offset: Int): PageDTO<Map<String, Product>> {
        val searchSession = Search.session(entityManager)

        val result: SearchResult<StorageProduct> = searchSession
            .search(StorageProduct::class.java)
            .where { f -> f.match().fields("information.name").matching(query).fuzzy(2) }
            .fetch(offset, 30) as SearchResult<StorageProduct>

        return PageDTO(result.hits().associate { it.toProduct() }, result.total().hitCount())
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun findById(id: String): Product? = db.findById(id).getOrNull()?.toProduct()?.second
    //= db.searchProducts(query)

    fun addAll(products: Map<String, Product>) =
        db.saveAll(products.map { StorageProduct(it.value, it.key) })

    fun deleteAll() = db.deleteAll()
}